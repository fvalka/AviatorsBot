package com.vektorraum.aviatorsbot.bot.subscriptions

import com.typesafe.scalalogging.Logger
import com.vektorraum.aviatorsbot.bot.weather.{FormatMetar, FormatTaf}
import com.vektorraum.aviatorsbot.generated.metar.METAR
import com.vektorraum.aviatorsbot.generated.taf.TAF
import com.vektorraum.aviatorsbot.persistence.subscriptions.SubscriptionDAO
import com.vektorraum.aviatorsbot.service.weather.AddsWeatherService
import info.mukel.telegrambot4s.models.Message

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SubscriptionHandler(subscriptionDAO: SubscriptionDAO, weatherService: AddsWeatherService,
                          send: (Long, String) => Future[Message]) {
  val logger = Logger(getClass)

  def run(): Unit = {
    logger.info("Handling subscriptions")
    getWeatherUpdates foreach {
      case (metars, tafs) =>
        val allStationNames = metars.keySet ++ tafs.keySet

        allStationNames foreach { station =>
          val subscriptionsFuture = subscriptionDAO.findAllSubscriptionsForStation(station)

          subscriptionsFuture map { subscriptions =>
            subscriptions map { sub =>
              send(sub.chatId, buildWxMessage(metars.get(sub.icao), tafs.get(sub.icao)))
            }
          }

        }
    }
  }

  private def buildWxMessage(metar: Option[Seq[METAR]], taf: Option[Seq[TAF]]) = {
    val metarFormatted = metar.map(FormatMetar(_)).getOrElse("")
    val tafFormatted = taf.map(FormatTaf(_)).getOrElse("")
    val separator = if(metarFormatted.nonEmpty && tafFormatted.nonEmpty) { "\n" } else { "" }

    metarFormatted + separator + tafFormatted
  }

  private def getWeatherUpdates: Future[(Map[String, Seq[METAR]], Map[String, Seq[TAF]])] = {
    logger.debug("Retrieving weather updates from weather service")
    subscriptionDAO.findAllStations flatMap { stations =>
      logger.info(s"Currently subscribed stations: $stations")
      for {
        metars <- weatherService.getMetars(stations)
        tafs <- weatherService.getTafs(stations)
      } yield (metars, tafs)
    }
  }
}
