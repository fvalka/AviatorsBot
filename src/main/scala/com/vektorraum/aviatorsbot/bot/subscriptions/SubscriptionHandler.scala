package com.vektorraum.aviatorsbot.bot.subscriptions

import com.typesafe.scalalogging.Logger
import com.vektorraum.aviatorsbot.bot.util.LatestInfoConverter
import com.vektorraum.aviatorsbot.bot.weather.{FormatMetar, FormatTaf}
import com.vektorraum.aviatorsbot.generated.metar.METAR
import com.vektorraum.aviatorsbot.generated.taf.TAF
import com.vektorraum.aviatorsbot.persistence.subscriptions.SubscriptionDAO
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.service.weather.AddsWeatherService
import info.mukel.telegrambot4s.models.Message

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class SubscriptionHandler(subscriptionDAO: SubscriptionDAO, weatherService: AddsWeatherService,
                          send: (Long, String) => Future[Message]) {
  val logger = Logger(getClass)

  def run(): Unit = {
    logger.info("Handling subscriptions")
    subscriptionDAO.findAllStations() onComplete {
      case Success(stations) =>
        if(stations.nonEmpty) {
          getWeatherUpdates(stations) onComplete {
            case Success((metars, tafs)) =>
              val allStationNames = metars.keySet ++ tafs.keySet

              allStationNames foreach { station =>
                val subscriptionsFuture = subscriptionDAO.findAllSubscriptionsForStation(station)

                subscriptionsFuture map { subscriptions =>
                  sendUpdatesForStation(metars, tafs, subscriptions)
                }
              }
            case Failure(t) =>
              logger.warn("Retrieving the weather updates for handling the subscriptions failed", t)
          }
        } else {
          logger.info("No current subscriptions found in database, skipping the handling of subscriptions")
        }
      case Failure(t) =>
        logger.warn("Could not get the list of stations from the database " +
          "while trying to handle the subscriptions", t)
    }
  }

  private def getWeatherUpdates(stations: Set[String]): Future[(Map[String, Seq[METAR]], Map[String, Seq[TAF]])] = {
    logger.debug("Retrieving weather updates from service")
    logger.info(s"Currently subscribed stations: $stations")
    for {
      metars <- weatherService.getMetars(stations)
      tafs <- weatherService.getTafs(stations)
    } yield (metars, tafs)
  }

  protected def sendUpdatesForStation(metars: Map[String, Seq[METAR]], tafs: Map[String, Seq[TAF]],
  subscriptions: List[Subscription]): Unit = {
    subscriptions foreach { sub =>
      val metar = metars.get(sub.icao)
      val taf = tafs.get(sub.icao)

      val latestMetar = metar.map { in => LatestInfoConverter.fromMetar(in.head) }
      val latestTaf = taf.map { in => LatestInfoConverter.fromTaf(in.head) }

      // Only send the metar if the date and hash do not match the ones already stored in the db
      val metarToSend = metar.flatMap { in => if (latestMetar == sub.latestMetar) { None } else { Some(in) } }
      val tafToSend = taf.flatMap { in => if (latestTaf == sub.latestTaf) { None } else { Some(in) } }

      if(metarToSend.nonEmpty || tafToSend.nonEmpty) {
        logger.debug(s"Sending weather update for sub=$sub")
        send(sub.chatId, buildWxMessage(metarToSend, tafToSend)) onComplete {
          case Success(msg) =>
            logger.debug(s"Subscription update message successfully sent and udating database for sub=$sub")
            if(latestMetar.nonEmpty) { sub.latestMetar = latestMetar }
            if(latestTaf.nonEmpty) { sub.latestTaf = latestTaf }

            subscriptionDAO.addOrExtend(sub)

          case Failure(e) => logger.warn("Subscription update message could not be " +
          s"sent for subscription=$sub")
        }
      } else {
        logger.debug(s"No updates to send for sub=$sub since both metarToSend and tafToSend were empty")
      }
    }
  }

  private def buildWxMessage(metar: Option[Seq[METAR]], taf: Option[Seq[TAF]]) = {
    val metarFormatted = metar.map(FormatMetar(_)).getOrElse("")
    val tafFormatted = taf.map(FormatTaf(_)).getOrElse("")
    val separator = if(metarFormatted.nonEmpty && tafFormatted.nonEmpty) { "\n" } else { "" }

    metarFormatted + separator + tafFormatted
  }
}
