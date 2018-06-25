package com.vektorraum.aviatorsbot.bot

import java.io.File
import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import com.vektorraum.aviatorsbot.service.weather.{AddsWeatherService, AddsWeatherServiceProduction}
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import com.softwaremill.macwire._
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import com.vektorraum.aviatorsbot.bot.util.{AliasCommands, HelpMessages, StationUtil, TimeFormatter}
import com.vektorraum.aviatorsbot.bot.weather.{FormatMetar, FormatTaf}
import com.vektorraum.aviatorsbot.bot.xwind.XWindCalculator
import com.vektorraum.aviatorsbot.generated.metar.METAR
import com.vektorraum.aviatorsbot.generated.taf.TAF
import com.vektorraum.aviatorsbot.persistence.airfielddata.{AirfieldDAO, AirfieldDAOProduction}
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.persistence.subscriptions.{SubscriptionDAO, SubscriptionDAOProduction}
import info.mukel.telegrambot4s.methods.ParseMode.ParseMode
import reactivemongo.api.commands.WriteResult

import scala.collection.mutable
import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success}

/**
  * Created by fvalka on 18.05.2017.
  */
trait AviatorsBot extends TelegramBot with Polling with AliasCommands {
  protected val trafficLog = Logger("traffic-log")

  protected val ERROR_INVALID_ICAO_LIST = "Please provide a valid ICAO station or list of stations e.g. \"wx LOWW " +
    "LOAV\""
  protected val ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED = "Subscriptions could not be stored. Please try again!"
  protected val ERROR_SUBSCRIPTIONS_COULD_NOT_BE_LISTED = "Subscriptions could not be listed. Please try again!"

  lazy val token: String = scala.util.Properties
    .envOrNone("BOT_TOKEN")
    .getOrElse(Source.fromFile("conf/bot.token").getLines().mkString)

  protected val config: Config = ConfigFactory.parseFile(new File("conf/aviatorsbot.conf"))

  protected lazy val weatherService: AddsWeatherService = wire[AddsWeatherServiceProduction]
  protected lazy val airfieldDAO: AirfieldDAO = wire[AirfieldDAOProduction]
  protected lazy val subscriptionDAO: SubscriptionDAO = wire[SubscriptionDAOProduction]

  on("/start") { implicit msg => _ => reply(HelpMessages("welcome")) }

  on("/wx", "Current weather") { implicit msg =>
    args =>
      if (!args.forall(StationUtil.isValidInput) || args.isEmpty) {
        reply(HelpMessages("wx"), ParseMode.HTML)
      } else {
        val stations = args.toList.map(station => station.toUpperCase())
        val message = for {
          metars <- weatherService.getMetars(stations)
          tafs <- weatherService.getTafs(stations)
        } yield {
          buildWxMessage(stations, metars, tafs)
        }

        message onComplete {
          case Success(m) => reply(m, parseMode = ParseMode.HTML)
          case Failure(t) => logger.warn(s"Exception thrown while running command=wx with args=$args", t)
            reply("Could not retrieve weather")
        }
      }
  }

  on("/xwind", "Current crosswind") { implicit msg =>
    args =>
      val station = args.head.toUpperCase
      if (args.size != 1 || !StationUtil.isICAOAptIdentifier(station)) {
        reply(HelpMessages("xwind"), ParseMode.HTML)
      } else {
        airfieldDAO.findByIcao(station) map {
          case Some(airfield) => weatherService.getMetars(List(station)) map { metars =>
            val metar = metars.get(station)

            metar match {
              case Some(m) => reply(
                s"METAR issued at: ${m.head.observation_time.get}\n" +
                  XWindCalculator(m.head, airfield), ParseMode.HTML)
              case None => reply("Could not retrieve weather for station")
            }
          }
          case None => reply("Airfield not found")
        } onComplete {
          case Success(_) => logger.info(s"XWind calculation performed successfully for station=$station")
          case Failure(t) => reply("Could not perform crosswind calculation for this station")
            logger.warn(s"Error during xwindCalculation for station=$station", t)
        }

      }
  }

  on("/add", "Subscribe to stations") { implicit msg =>
    args =>
      if (!args.forall(StationUtil.isICAOAptIdentifier) || args.isEmpty) {
        reply(HelpMessages("add"), ParseMode.HTML)
      } else {
        val stations = args.map(_.toUpperCase)
        val validHours = config.getConfig("subscriptions").getInt("validHoursDefault")
        val validUntil = ZonedDateTime.now(ZoneOffset.UTC).plusHours(validHours)
        val insertFutures: Seq[Future[WriteResult]] = stations map { station =>
          subscriptionDAO.addOrExtend(Subscription(msg.chat.id, station, Date.from(validUntil.toInstant)))
        }

        // double check that the insertion worked
        Future.sequence(insertFutures) onComplete {
          case Success(writeResults) =>
            if (writeResults.forall(_.ok)) {
              reply(s"Subscription is active until:\n${TimeFormatter.shortUTCDateTimeFormat(validUntil)}")
            } else {
              logger.warn(s"Write failed for some stations (writeResult.ok==false) msg=$msg args=$args " +
                s"writeResults=$writeResults")
              reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED)
            }
          case Failure(t) =>
            logger.warn(s"Write failed for some stations (Future failed) msg=$msg args=$args", t)
            reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED)
        }
      }
  }

  on("/ls", "List all subscriptions") { implicit msg =>
    args =>
      subscriptionDAO.findAllByChatId(msg.chat.id) onComplete {
        case Success(subscriptions) =>
          val subsFormatted = (subscriptions map { sub =>
            val icao = sub.icao.toUpperCase
            val validUntil = TimeFormatter.shortUTCDateTimeFormat(
              ZonedDateTime.ofInstant(sub.validUntil.toInstant, ZoneOffset.UTC))

            s"<strong>$icao</strong> - $validUntil"
          }) mkString "\n"

          val result = if (!subsFormatted.isEmpty) {
            "<strong>ICAO  - valid until</strong>\n" + subsFormatted
          } else {
            "No active subscriptions"
          }

          reply(result, ParseMode.HTML)
        case Failure(t) => logger.warn(s"Could not retrieve subscriptions for chatId=${msg.chat.id}, " +
          s"msg=$msg args=$args", t)
          reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_LISTED)
      }
  }

  protected def buildWxMessage(stations: List[String],
                               metars: Map[String, Seq[METAR]],
                               tafs: Map[String, Seq[TAF]]): String = {
    // Support for wild-cards in station lists
    val inputStationsSet = mutable.LinkedHashSet(stations.filter(StationUtil.isICAOAptIdentifier): _*)
    val stationSet = inputStationsSet ++ metars.keySet

    stationSet.map(station => {
      val metar = metars.get(station) match {
        case Some(mt) => FormatMetar(mt)
        case None => s"<strong>$station</strong> No METAR received for station"
      }
      val taf = tafs.get(station) match {
        case Some(tf) => FormatTaf(tf)
        case None => s"<strong>TAF $station</strong> No TAF received for station"
      }
      metar + "\n" + taf
    }) mkString "\n"
  }

  override def reply(text: String, parseMode: Option[ParseMode],
                     disableWebPagePreview: Option[Boolean],
                     disableNotification: Option[Boolean],
                     replyToMessageId: Option[Long],
                     replyMarkup: Option[ReplyMarkup])
                    (implicit message: Message): Future[Message] = {
    trafficLog.info(s"Outbound messageId=${message.messageId} - chatId=${message.chat.id} - " +
      s"chatUserName=${message.chat.username} - inboundMessage=${message.text} - text=$text")
    super.reply(text, parseMode, disableWebPagePreview, disableNotification, replyToMessageId, replyMarkup)
  }


}
