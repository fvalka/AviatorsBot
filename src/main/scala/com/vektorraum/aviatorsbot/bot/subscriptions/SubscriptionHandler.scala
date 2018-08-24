package com.vektorraum.aviatorsbot.bot.subscriptions

import java.io.File
import java.lang.Exception

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import com.vektorraum.aviatorsbot.bot.util.LatestInfoConverter
import com.vektorraum.aviatorsbot.bot.weather.{FormatMetar, FormatTaf}
import com.vektorraum.aviatorsbot.generated.metar.METAR
import com.vektorraum.aviatorsbot.generated.taf.TAF
import com.vektorraum.aviatorsbot.persistence.subscriptions.SubscriptionDAO
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.service.weather.AddsWeatherService
import info.mukel.telegrambot4s.models.Message
import nl.grons.metrics4.scala.DefaultInstrumented

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

/**
  * Handles the sending of weather updates to subscribers.
  *
  * @param subscriptionDAO Persistence backend
  * @param weatherService Weather service providing METARs and TAFs
  * @param send Updates will be sent using this function
  */
class SubscriptionHandler(subscriptionDAO: SubscriptionDAO, weatherService: AddsWeatherService,
                          send: (Long, String) => Future[Message], configComplete: Config) extends DefaultInstrumented {
  // LOGGING
  private val logger = Logger(getClass)

  // METRICS
  private val timerAllSubscriptions = metrics.timer("all-subscriptions")
  private val messagesSent = metrics.meter("messages-sent")
  private val messageFailures = metrics.meter("messages-sent-failed")
  private val messagesInTransit = metrics.counter("messages-in-transit")
  private val weatherServiceFailures = metrics.meter("weather-service-failed")
  private val databaseFailures = metrics.meter("db-failures")

  // CONFIGURATION
  protected val config: Config = configComplete.getConfig("subscriptions.handler")

  def run(): Unit = timerAllSubscriptions.time {
    logger.info("Handling subscriptions")
    val result = subscriptionDAO.findAllStations() transformWith {
      case Success(stations) =>
        if(stations.nonEmpty) {
          getWeatherUpdates(stations) transformWith {
            case Success((metars, tafs)) =>
              val allStationNames = metars.keySet ++ tafs.keySet

              val allMsgResults = allStationNames map { station =>
                val subscriptionsFuture = subscriptionDAO.findAllSubscriptionsForStation(station)

                subscriptionsFuture flatMap { subscriptions =>
                  sendUpdatesForStation(metars, tafs, subscriptions)
                }
              }
              Future.sequence(allMsgResults)
            case Failure(t) =>
              logger.warn("Retrieving the weather updates for handling the subscriptions failed", t)
              weatherServiceFailures.mark()
              Future.failed(new RetriableException(t, config.getInt("retries_weather_service")))
          }
        } else {
          logger.info("No current subscriptions found in database, skipping the handling of subscriptions")
          Future.successful()
        }
      case Failure(t) =>
        logger.warn("Could not get the list of stations from the database " +
          "while trying to handle the subscriptions", t)
        databaseFailures.mark()
        Future.failed(new RetriableException(t, config.getInt("retries_db")))
    }

    // Block so that the poller isn't started twice while futures are still being executed
    Await.result(result, Duration(config.getString("run_timeout")))
  }

  private def getWeatherUpdates(stations: Set[String]): Future[(Map[String, Seq[METAR]], Map[String, Seq[TAF]])] = {
    logger.debug("Retrieving weather updates from service")
    logger.info(s"Currently subscribed stations: $stations")
    for {
      metars <- weatherService.getMetars(stations)
      tafs <- weatherService.getTafs(stations)
    } yield (metars, tafs)
  }

  private def sendUpdatesForStation(metars: Map[String, Seq[METAR]], tafs: Map[String, Seq[TAF]],
  subscriptions: List[Subscription]): Future[Seq[Any]] = {
    val result: Seq[Future[Any]] = subscriptions map { sub =>
      val metar = metars.get(sub.icao)
      val taf = tafs.get(sub.icao)

      val latestMetar = metar.map { in => LatestInfoConverter.fromMetar(in.head) }
      val latestTaf = taf.map { in => LatestInfoConverter.fromTaf(in.head) }

      // Only send the metar if the date and hash do not match the ones already stored in the db
      // and if the user has subscribed to the metar and taf
      val metarToSend = metar.flatMap { in =>
        if (latestMetar == sub.latestMetar || !sub.metar) { None } else { Some(in) }
      }
      val tafToSend = taf.flatMap { in =>
        if (latestTaf == sub.latestTaf || !sub.taf) { None } else { Some(in) }
      }

      if(metarToSend.nonEmpty || tafToSend.nonEmpty) {
        logger.debug(s"Sending weather update for sub=$sub")
        messagesSent.mark()
        messagesInTransit += 1
        send(sub.chatId, buildWxMessage(metarToSend, tafToSend)) transformWith {
          case Success(msg) =>
            logger.debug(s"Subscription update message successfully sent and udating database for sub=$sub")
            messagesInTransit -= 1

            if(latestMetar.nonEmpty) { sub.latestMetar = latestMetar }
            if(latestTaf.nonEmpty) { sub.latestTaf = latestTaf }

            subscriptionDAO.addOrExtend(sub)
          case Failure(e) =>
            logger.warn(s"Subscription update message could not be sent for subscription=$sub")
            messagesInTransit -= 1
            messageFailures.mark()
            Future.failed(e)
        }
      } else {
        logger.debug(s"No updates to send for sub=$sub since both metarToSend and tafToSend were empty")
        Future.successful()
      }
    }

    Future.sequence(result)
  }

  private def buildWxMessage(metar: Option[Seq[METAR]], taf: Option[Seq[TAF]]) = {
    val metarFormatted = metar.map(FormatMetar(_)).getOrElse("")
    val tafFormatted = taf.map(FormatTaf(_)).getOrElse("")
    val separator = if(metarFormatted.nonEmpty && tafFormatted.nonEmpty) { "\n" } else { "" }

    metarFormatted + separator + tafFormatted
  }
}
