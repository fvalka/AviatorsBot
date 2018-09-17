package com.vektorraum.aviatorsbot.bot

import java.io.File
import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date
import java.util.concurrent.atomic.AtomicLong

import com.softwaremill.macwire._
import com.typesafe.config.{Config, ConfigFactory}
import com.vektorraum.aviatorsbot.bot.calculators.{DensityAltitudeCalculator, XWindCalculator}
import com.vektorraum.aviatorsbot.bot.commands.{Command, InstrumentedCommands}
import com.vektorraum.aviatorsbot.bot.subscriptions.SubscriptionHandler
import com.vektorraum.aviatorsbot.bot.util._
import com.vektorraum.aviatorsbot.bot.weather.BuildWxMessage
import com.vektorraum.aviatorsbot.persistence.airfielddata.{AirfieldDAO, AirfieldDAOProduction}
import com.vektorraum.aviatorsbot.persistence.regions.model.RegionSetting
import com.vektorraum.aviatorsbot.persistence.regions.{RegionsDAO, RegionsDAOProduction}
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.persistence.subscriptions.{SubscriptionDAO, SubscriptionDAOProduction}
import com.vektorraum.aviatorsbot.persistence.{Db, WriteResult}
import com.vektorraum.aviatorsbot.service.regions.Regions
import com.vektorraum.aviatorsbot.service.strikes.{StrikesService, StrikesServiceProduction}
import com.vektorraum.aviatorsbot.service.weather.{AddsWeatherService, AddsWeatherServiceProduction}
import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import nl.grons.metrics4.scala.DefaultInstrumented

import scala.concurrent.Future
import scala.io.Source
import scala.language.postfixOps
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

/**
  * Main Bot trait which is both used for testing and production
  *
  * Handles commands received by the bot using the onCommand() functions
  */
trait AviatorsBot
  extends TelegramBot
    with Polling
    with InstrumentedCommands
    with DefaultInstrumented
    with Args {
  // STATIC STRINGS
  protected val ERROR_INVALID_ICAO_LIST: String = "Please provide a valid ICAO station or list of stations e.g. \"wx " +
    "LOWW " +
    "LOAV\""
  protected val ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED = "Subscriptions could not be stored. Please try again!"
  protected val ERROR_SUBSCRIPTIONS_COULD_NOT_BE_LISTED = "Subscriptions could not be listed. Please try again!"
  protected val ERROR_SUBSCRIPTIONS_COULD_NOT_BE_REMOVED = "Could not unsubscribe from all stations. Please try again!"
  protected val ERROR_NO_METAR_FOR_STATION = "Could not retrieve weather for station"
  protected val ERROR_REGION_UPDATE_FAILED = "Could not store preference. Please try again later!"

  // separated from the main configuration for security reasons
  lazy val token: String = scala.util.Properties
    .envOrNone("BOT_TOKEN")
    .getOrElse(Source.fromFile("conf/bot.token").getLines().mkString)

  protected val config: Config = ConfigFactory.parseFile(new File("conf/aviatorsbot.conf"))

  protected val defaultRegion: Regions = Regions.withValue(config.getString("regions.default"))

  // EXTERNAL SERVICES
  protected lazy val db: Db = wire[Db]
  protected lazy val weatherService: AddsWeatherService = wire[AddsWeatherServiceProduction]
  protected lazy val strikesService: StrikesService = wire[StrikesServiceProduction]
  protected lazy val airfieldDAO: AirfieldDAO = wire[AirfieldDAOProduction]
  protected lazy val subscriptionDAO: SubscriptionDAO = wire[SubscriptionDAOProduction]
  protected lazy val regionsDAO: RegionsDAO = wire[RegionsDAOProduction]
  // Needed so that macwire can find the send function which has to be passed into the constructor
  // of SubscriptionHandler
  protected lazy val sendFunc: (Long, String) => Future[Message] = send
  lazy val subscriptionHandler: SubscriptionHandler = wire[SubscriptionHandler]
  lazy val densityAltitudeCalculator: DensityAltitudeCalculator = wire[DensityAltitudeCalculator]


  onCommand(Command("start", "Information about this bot", "Info", anyArgs)) {
    implicit msg =>
      _ =>
        val helpMessage = HelpMessages("welcome") + "\n" +
          commandList() +
          "\n\nSend /help &lt;command&gt; to learn more about a command."
        reply(helpMessage, disableWebPagePreview = true, parseMode = ParseMode.HTML)
  }

  onCommand(Command("privacy", "Privacy policy", "Info", anyArgs)) {
    implicit msg =>
      _ =>
        reply(HelpMessages("privacy"),
          disableWebPagePreview = true, parseMode = ParseMode.HTML)
  }

  onCommand(Command("region", "Preferred region", "Settings", regionOptionalArgs)) {
    def setRegion(implicit msg: Message, region: Regions) = {
      regionsDAO.set(RegionSetting(msg.chat.id, region)) andThen {
        case Success(writeResult) =>
          if (writeResult.ok) {
            reply("Preferred region updated")
          } else {
            logger.warn(s"Region update failed with writeResult.ok false msg=$msg")
            reply(ERROR_REGION_UPDATE_FAILED)
          }
        case Failure(exception) =>
          logger.warn(s"Region update failed with failed future msg=$msg", exception)
          reply(ERROR_REGION_UPDATE_FAILED)
      }
    }

    def listRegions(implicit msg: Message) = {
      regionsDAO.get(msg.chat.id) flatMap { regionInDb =>
        val current = regionInDb
          .map(dbValue => dbValue.region.description)
          .getOrElse("Default: " + defaultRegion.description)

        val regionString = Regions.values
          .sortBy(_.value)
          .map(region => region.value + " - " + region.description)
          .mkString("\n")

        reply("<strong>Current region:</strong> " + current + "\n\n" +
          "<strong>Available regions:</strong>\n" +
          regionString +
          "\n\nUse /region &lt;code&gt; to set your preference.",
          ParseMode.HTML)
      } recoverWith {
        case NonFatal(t) => logger.warn(s"Region could not be retrieved from the database msg=$msg", t)
        reply("Could not get current region from the database")
      }
    }

    implicit msg =>
      args =>
        regionFromArgs(args) match {
          case Some(region) =>
            setRegion(msg, region)
          case None =>
            listRegions(msg)
        }
  }

  onCommand(Command("wx", "Current METAR and TAF", "Weather", weatherServiceStationsArgs, longRunning = true)) {
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

  onCommand(Command("strikes", "Lightning strikes", "Weather Charts", regionOptionalArgs, longRunning = true)) {
    implicit msg =>
      args =>
        regionPreference(msg.chat.id, args) flatMap { region =>
          strikesService.getUrl(region) match {
            case Some(url) => sendPhoto(msg.chat.id, url)
            case None =>
              logger.info(s"Strikes requested for region which doesn't exist region=$region and msg=$msg")
              reply("No strikes available for this region")
          }
        }
  }

  onCommand(Command("xwind", "Current crosswind", "Calculations", oneStationArgs, longRunning = true)) {
    implicit msg =>
      args =>
        val station = args("station").head
        airfieldDAO.findByIcao(station) map {
          case Some(airfield) => weatherService.getMetars(List(station)) map { metars =>
            val metar = metars.get(station)

            metar match {
              case Some(m) =>
                val observationTime = TimeFormatter.shortUTCDateTimeFormat(m.head.observation_time.get)
                reply(
                  s"METAR issued at: $observationTime\n" +
                    "Direction of flight: ⬆\n" +
                    XWindCalculator(m.head, airfield), ParseMode.HTML)
              case None => reply(ERROR_NO_METAR_FOR_STATION)
            }
          }
          case None => reply("Airfield not found")
        } andThen {
          case Success(_) => logger.debug(s"XWind calculation performed successfully for station=$station")
          case Failure(t) => reply(s"Could not perform crosswind calculation for this station")
            logger.warn(s"Error during xwindCalculation for station=$station", t)
        }
  }

  onCommand(Command("da", "Density altitude", "Calculations", oneStationArgs, longRunning = true)) {
    implicit msg =>
      args =>
        val station = args("station").head
        weatherService.getMetars(List(station)) map { metars =>
          val metar = metars.get(station)
          metar match {
            case Some(m) =>
              densityAltitudeCalculator(m.head) andThen {
                case Success(message) => reply(message, ParseMode.HTML)
                case Failure(t) => reply("Could not perform density altitude calculation for this station")
                  logger.warn("DensityAltitudeCalculator returned failure", t)
              }
            case None => reply(ERROR_NO_METAR_FOR_STATION)
          }
        } andThen {
          case Success(_) => logger.debug(s"Density altitude calculation successful for station=$station")
          case Failure(t) => reply(s"Could not perform density altitude calculation for this station")
            logger.warn(s"Error during density altitude calculation for station=$station")
        }
  }

  onCommand(Command("add", "Subscribe to stations", "Subscriptions", stationsArgs ++ oneTimeArgs ++ metarTafArgs)) {
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
            Date.from(validUntil.toInstant), metar = metar, taf = taf)
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

  onCommand(Command("rm", "Unsubscribe from stations", "Subscriptions", wildcardStationArgs)) {
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

  onCommand(Command("ls", "List all subscriptions", "Subscriptions")) {
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

  /**
    * Get the region for the execution of this command.
    *
    * The following order is used:
    * 1. Args - If the args contain a valid region this is used
    * 2. Database - If the args didn't contain a region the users RegionSetting is looked up
    * 3. Default Region - If the user doesn't have a region setting stored the default region is used
    *
    * @param chatId For whom the region preference will be determined
    * @param args Arguments sent to the command
    * @return Region obtained as described above
    */
  protected def regionPreference(chatId: Long, args: Map[String, Seq[String]]): Future[Regions] = {
    val argRegion = regionFromArgs(args)

    val result = if(argRegion.isDefined) {
      Future.successful(argRegion)
    } else {
      regionsDAO.get(chatId)
        .map(_.map(_.region))
        .recover {
          case NonFatal(t) =>
            logger.warn(s"Retrieving the region preference from the database failed in regionPreference chatId=$chatId", t)
            Some(defaultRegion)
        }
    }

    result.map(_.getOrElse(defaultRegion))
  }

  /**
    * Get the region set in the command arguments, if present
    *
    * @param args Command arguments
    * @return Region set in the arguments, if present
    */
  protected def regionFromArgs(args: Map[String, Seq[String]]): Option[Regions] = {
    args.get("region")
      .flatMap(_.headOption)
      .flatMap(RegionUtil.find)
  }

  // METRICS
  protected val subscriptionCount = new AtomicLong()
  metrics.gauge[Long]("subscription-count") {
    subscriptionDAO.count() foreach { count => subscriptionCount.set(count) }
    subscriptionCount.get()
  }


}
