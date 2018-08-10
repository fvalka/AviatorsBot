package com.vektorraum.aviatorsbot.bot

import java.io.File
import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import com.softwaremill.macwire._
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import com.vektorraum.aviatorsbot.bot.util.{AliasCommands, HelpMessages, StationUtil, TimeFormatter}
import com.vektorraum.aviatorsbot.bot.weather.BuildWxMessage
import com.vektorraum.aviatorsbot.bot.xwind.XWindCalculator
import com.vektorraum.aviatorsbot.persistence.Db
import com.vektorraum.aviatorsbot.persistence.airfielddata.{AirfieldDAO, AirfieldDAOProduction}
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.persistence.subscriptions.{SubscriptionDAO, SubscriptionDAOProduction, WriteResult}
import com.vektorraum.aviatorsbot.service.weather.{AddsWeatherService, AddsWeatherServiceProduction}
import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.methods.ParseMode.ParseMode
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._

import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success}

/**
  * Main Bot trait which is both used for testing and production
  *
  * Handles all the commands received by the bot using the on..() functions
  */
trait AviatorsBot extends TelegramBot with Polling with AliasCommands {
  protected val trafficLog = Logger("traffic-log")

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

  protected lazy val db: Db = wire[Db]
  protected lazy val weatherService: AddsWeatherService = wire[AddsWeatherServiceProduction]
  protected lazy val airfieldDAO: AirfieldDAO = wire[AirfieldDAOProduction]
  protected lazy val subscriptionDAO: SubscriptionDAO = wire[SubscriptionDAOProduction]

  on("/start") { implicit msg => _ => reply(HelpMessages("welcome")) }

  onStations("/wx", "Current weather") { implicit msg =>
    stations =>
      val message = for {
        metars <- weatherService.getMetars(stations.toList)
        tafs <- weatherService.getTafs(stations.toList)
      } yield {
        BuildWxMessage(stations.toList, metars, tafs)
      }

      message onComplete {
        case Success(m) => reply(m, parseMode = ParseMode.HTML)
        case Failure(t) => logger.warn(s"Exception thrown while running command=wx with stations=$stations", t)
          reply("Could not retrieve weather")
      }
  }

  onStation("/xwind", "Current crosswind") { implicit msg =>
    stations =>
      val station = stations.head
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

  onStations("/add", "Subscribe to stations") { implicit msg =>
    stations =>
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
            logger.warn(s"Write failed for some stations (writeResult.ok==false) msg=$msg stations=$stations " +
              s"writeResults=$writeResults")
            reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED)
          }
        case Failure(t) =>
          logger.warn(s"Write failed for some stations (Future failed) msg=$msg stations=$stations", t)
          reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED)
      }
  }

  onStations("/rm", "Unsubscribe from a station") { implicit msg =>
    stations =>
      val removeFutures = stations map { station =>
        subscriptionDAO.remove(msg.chat.id, station)
      }

    Future.sequence(removeFutures) onComplete {
      case Success(writeResults) =>
        if(writeResults.forall(_.ok)) {
          reply("Unsubscribed successfully")
        } else {
          logger.warn(s"Could not remove some subscriptions (writeResults.ok==false) msg=$msg " +
            s"stations=$stations writeResults=$writeResults")
          reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_REMOVED)
        }
      case Failure(t) =>
        logger.warn(s"Removing subscriptions failed with Future failed msg=$msg stations=$stations", t)
    }
  }

  onNoArguments("/ls", "List all subscriptions") { implicit msg =>
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
        case Failure(t) =>
          logger.warn(s"Could not retrieve subscriptions for chatId=${msg.chat.id}, " +
          s"msg=$msg args=$args", t)
          reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_LISTED)
      }
  }

  /**
    * Registers a func handling the command without arguments
    *
    * @param command Bot command e.g. "/wx", "/add", etc.
    * @param description Description shown to the user in the help command
    * @param func Function called with the message and stations
    */
  protected def onNoArguments(command: String, description: String)
                          (func: Message => Seq[String] => Unit): Unit =
    onVerifyInput(_.isEmpty, identity)(command, description)(func)

  /**
    * Registers a func handling the command with only valid ICAO stations as its argument
    *
    * @param command Bot command e.g. "/wx", "/add", etc.
    * @param description Description shown to the user in the help command
    * @param func Function called with the message and stations
    */
  protected def onStations(command: String, description: String)
                             (func: Message => Seq[String] => Unit): Unit =
    onVerifyInput(args => args.forall(StationUtil.isICAOAptIdentifier) && args.nonEmpty,
      _.map(_.toUpperCase))(command, description)(func)

  /**
    * Registers a func handling the command with only exactly 1 ICAO station as its argument
    *
    * @param command Bot command e.g. "/wx", "/add", etc.
    * @param description Description shown to the user in the help command
    * @param func Function called with the message and stations
    */
  protected def onStation(command: String, description: String)
                          (func: Message => Seq[String] => Unit): Unit =
    onVerifyInput(args => args.length == 1 && StationUtil.isICAOAptIdentifier(args.head),
      _.map(_.toUpperCase))(command, description)(func)

  /**
    * Verifies that all the args are valid and sends the
    * help message for this command if
    *
    * @param verifier Function which verifies that the args are valid
    * @param transformer Transforms the args before they are passed to func
    * @param command Bot command e.g. "/ls", "/rm", etc.
    * @param description Description given in the overall help message
    * @param func Curried function which is executed if all stations are valid
    */
  protected def onVerifyInput(verifier: Seq[String] => Boolean, transformer: Seq[String] => Seq[String])
                             (command: String, description: String)
                          (func: Message => Seq[String] => Unit): Unit = {
    on(command, description) { implicit msg =>
      args =>
        if (!verifier(args)) {
          val helpFileName = command.replace("/", "")
          reply(HelpMessages(helpFileName), ParseMode.HTML)
        } else {
          func(msg)(transformer(args))
        }
    }
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
