package com.vektorraum.aviatorsbot.bot

import java.io.File
import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import com.softwaremill.macwire._
import com.typesafe.config.{Config, ConfigFactory}
import com.vektorraum.aviatorsbot.bot.commands.{Argument, Command, InstrumentedCommands}
import com.vektorraum.aviatorsbot.bot.subscriptions.SubscriptionHandler
import com.vektorraum.aviatorsbot.bot.util._
import com.vektorraum.aviatorsbot.bot.weather.BuildWxMessage
import com.vektorraum.aviatorsbot.bot.xwind.XWindCalculator
import com.vektorraum.aviatorsbot.persistence.Db
import com.vektorraum.aviatorsbot.persistence.airfielddata.{AirfieldDAO, AirfieldDAOProduction}
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.persistence.subscriptions.{SubscriptionDAO, SubscriptionDAOProduction, WriteResult}
import com.vektorraum.aviatorsbot.service.weather.{AddsWeatherService, AddsWeatherServiceProduction}
import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import nl.grons.metrics4.scala.DefaultInstrumented

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.Source
import scala.util.{Failure, Success}

/**
  * Main Bot trait which is both used for testing and production
  *
  * Handles all the commands received by the bot using the on..() functions
  */
trait AviatorsBot extends TelegramBot with Polling with InstrumentedCommands with DefaultInstrumented {
  // STATIC STRINGS
  protected val ERROR_INVALID_ICAO_LIST: String = "Please provide a valid ICAO station or list of stations e.g. \"wx LOWW " +
    "LOAV\""
  protected val ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED = "Subscriptions could not be stored. Please try again!"
  protected val ERROR_SUBSCRIPTIONS_COULD_NOT_BE_LISTED = "Subscriptions could not be listed. Please try again!"
  protected val ERROR_SUBSCRIPTIONS_COULD_NOT_BE_REMOVED = "Could not unsubscribe from all stations. Please try again!"

  // separated from the main configuration for security reasons
  lazy val token: String = scala.util.Properties
    .envOrNone("BOT_TOKEN")
    .getOrElse(Source.fromFile("conf/bot.token").getLines().mkString)

  protected val config: Config = ConfigFactory.parseFile(new File("conf/aviatorsbot.conf"))

  // EXTERNAL SERVICES
  protected lazy val db: Db = wire[Db]
  protected lazy val weatherService: AddsWeatherService = wire[AddsWeatherServiceProduction]
  protected lazy val airfieldDAO: AirfieldDAO = wire[AirfieldDAOProduction]
  protected lazy val subscriptionDAO: SubscriptionDAO = wire[SubscriptionDAOProduction]
  // Needed so that macwire can find the send function which has to be passed into the constructor
  // of SubscriptionHandler
  protected lazy val sendFunc: (Long, String) => Future[Message] = send
  protected lazy val subscriptionHandler: SubscriptionHandler = wire[SubscriptionHandler]

  // METRICS
  metrics.cachedGauge("subscription-count", timeout = 3 minutes) { subscriptionDAO.count() }

  // COMMAND ARGUMENTS
  // Requires at least one ICAO code and allows adds wildcards like LO*, etc.
  protected val wildcardStationsArgs = Set(Argument("stations", StationUtil.isValidInput,
    min = 1, preprocessor = _.toUpperCase))
  // Requires at lest one ICAO code and only allows actual stations e.g. LOWW, KJFK, etc.
  protected val stationsArgs = Set(Argument("stations", StationUtil.isICAOAptIdentifier,
    min = 1, preprocessor = _.toUpperCase))
  // Requires exactly one actual ICAO code
  protected val oneStationArgs = Set(Argument("station", StationUtil.isICAOAptIdentifier,
    min = 1, max = 1, preprocessor = _.toUpperCase))
  // Time or duration argument
  protected val oneTimeArgs = Set(Argument("time", TimeUtil.isTimeOrDuration, max = 1))
  // METAR and/or TAF option for setting subscription options
  protected val metarTafArgs = Set(Argument("metartaf", MetarTafOption.valid, max = 1))


  onCommand(Command("start", "Information about this bot")) {
    implicit msg => _ => reply(HelpMessages("welcome"))
  }

  onCommand(Command("wx", "Current METAR and TAF", wildcardStationsArgs, longRunning = true)) {
    implicit msg =>
      args =>
        val stations = args("stations")
        val message = for {
          metars <- weatherService.getMetars(stations.toList)
          tafs <- weatherService.getTafs(stations.toList)
        } yield {
          BuildWxMessage(stations.toList, metars, tafs)
        }

        message andThen {
          case Success(m) => reply(m, parseMode = ParseMode.HTML)
          case Failure(t) => logger.warn(s"Exception thrown while running command=wx with stations=$stations", t)
            reply("Could not retrieve weather")
        }
  }

  onCommand(Command("xwind", "Current crosswind", oneStationArgs, longRunning = true)) {
    implicit msg =>
      args =>
        val station = args("station").head
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
        } andThen {
          case Success(_) => logger.info(s"XWind calculation performed successfully for station=$station")
          case Failure(t) => reply("Could not perform crosswind calculation for this station")
            logger.warn(s"Error during xwindCalculation for station=$station", t)
        }
  }

  onCommand(Command("add", "Subscribe to stations", stationsArgs ++ oneTimeArgs ++ metarTafArgs)) {
    implicit msg =>
      args =>
        val stations = args("stations")
        val validUntil = args.get("time")
          .flatMap(_.headOption)
          .map(TimeUtil.parseDurationOrTime)
          .getOrElse {
            val validHours = config.getConfig("subscriptions").getInt("validHoursDefault")
            ZonedDateTime.now(ZoneOffset.UTC).plusHours(validHours)
          }

        val metar = args.get("metartaf")
          .flatMap(_.exists(MetarTafOption.isMetar))
          .getOrElse(true)
        val taf = args.get("metartaf")
          .flatMap(_.exists(MetarTafOption.isTaf))
          .getOrElse(true)

        val insertFutures: Seq[Future[WriteResult]] = stations map { station =>
          val subscription = Subscription(msg.chat.id, station,
            Date.from(validUntil.toInstant), metar=metar, taf=taf)
          subscriptionDAO.addOrExtend(subscription)
        }

        // double check that the insertion worked
        Future.sequence(insertFutures) andThen {
          case Success(writeResults) =>
            if (writeResults.forall(_.ok)) {
              reply(s"Subscription is active until:\n${TimeFormatter.shortUTCDateTimeFormat(validUntil)}")
            } else {
              logger.warn(s"Write failed for some stations (writeResult.ok==false) msg=$msg stations=$stations " +
                s"writeResults=$writeResults")
              reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED)
            }
          case Failure(t) =>
            logger.warn(s"Write failed for some stations (Future failed) msg=$msg stations=$stations", t)
            reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED)
        }
  }

  onCommand(Command("rm", "Unsubscribe from stations", stationsArgs)) {
    implicit msg =>
      args =>
        val stations = args("stations")
        val removeFutures = stations map { station =>
          subscriptionDAO.remove(msg.chat.id, station)
        }

        Future.sequence(removeFutures) andThen {
          case Success(writeResults) =>
            if (writeResults.forall(_.ok)) {
              reply("Unsubscribed successfully")
            } else {
              logger.warn(s"Could not remove some subscriptions (writeResults.ok==false) msg=$msg " +
                s"stations=$stations writeResults=$writeResults")
              reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_REMOVED)
            }
          case Failure(t) =>
            logger.warn(s"Removing subscriptions failed with Future failed msg=$msg stations=$stations", t)
            reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_REMOVED)
        }
  }

  onCommand(Command("ls", "List all subscriptions")) {
    implicit msg =>
      args =>
        subscriptionDAO.findAllByChatId(msg.chat.id) andThen {
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
          case Failure(t) =>
            logger.warn(s"Could not retrieve subscriptions for chatId=${msg.chat.id}, " +
              s"msg=$msg args=$args", t)
            reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_LISTED)
        }
  }


}
