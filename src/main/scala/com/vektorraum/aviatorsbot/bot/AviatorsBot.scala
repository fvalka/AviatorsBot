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
import com.vektorraum.aviatorsbot.bot.util.{AliasCommands, StationUtil, TimeFormatter}
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

  protected val ERROR_INVALID_ICAO_LIST = "Please provide a valid ICAO station or list of stations e.g. \"wx LOWW LOAV\""
  protected val ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED = "Subscriptions could not be stored. Please try again!"

  protected val WelcomeMessage: String = "Welcome to vektorraum AviatorsBot!\n\n" +
    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, " +
    "INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR " +
    "PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES " +
    "OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, " +
    "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n\n" +
    "THIS SOFTWARE IS NOT AN OFFICIAL BRIEFING SOURCE. ANY DATA SENT MIGHT BE WRONG, " +
    "OUT OF DATE OR OTHERWISE UNUSABLE OR MISLEADING, NO GUARANTEES CAN BE MADE ABOUT THE " +
    "AVAILABILITY OF THIS SERVICE, ESPECIALLY THE POLLING/SUBSCRIPTION MECHANISM\n" +
    "USE PURELY AT YOUR OWN RISK!"

  lazy val token: String = scala.util.Properties
    .envOrNone("BOT_TOKEN")
    .getOrElse(Source.fromFile("conf/bot.token").getLines().mkString)

  protected val config: Config = ConfigFactory.parseFile(new File("conf/aviatorsbot.conf"))

  protected lazy val weatherService: AddsWeatherService = wire[AddsWeatherServiceProduction]
  protected lazy val airfieldDAO: AirfieldDAO = wire[AirfieldDAOProduction]
  protected lazy val subscriptionDAO: SubscriptionDAO = wire[SubscriptionDAOProduction]

  on("hello") { implicit msg => _ => reply("My token is SAFE!") }

  on("start") { implicit msg => _ => reply(WelcomeMessage) }

  on("wx", "METAR for multiple stations") { implicit msg =>
    args =>
      if (!args.forall(StationUtil.isValidInput)) {
        reply(ERROR_INVALID_ICAO_LIST)
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
            reply("Could not retrieve METARs")
        }
      }
  }

  on("xwind", "Crosswind for a station") { implicit msg =>
    args =>
      val station = args.head.toUpperCase
      if (args.size != 1 || !StationUtil.isICAOAptIdentifier(station)) {
        reply("Please provide a valid ICAO airport identifier")
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

  on("add", "Subscribe to stations") { implicit msg =>
    args =>
      if (!args.forall(StationUtil.isICAOAptIdentifier)) {
        reply(ERROR_INVALID_ICAO_LIST)
      } else {
        val validHours = config.getConfig("subscriptions").getInt("validHoursDefault")
        val validUntil = ZonedDateTime.now(ZoneOffset.UTC).plusHours(validHours)
        val insertFutures: Seq[Future[WriteResult]] = args map { arg =>
          subscriptionDAO.addOrUpdate(Subscription(msg.chat.id, arg, Date.from(validUntil.toInstant)))
        }

        // double check that the insertion worked
        Future.sequence(insertFutures) onComplete {
          case Success(writeResults) =>
            if (writeResults.forall(_.ok)) {
              reply(s"Subscription is active until:\n${TimeFormatter.shortUTCDateTimeFormat(validUntil)}")
            } else {
              logger.warn(s"Write failed for some stations (writeResult.ok==false) msg=$msg args=$args writeResults=$writeResults")
              reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED)
            }
          case Failure(t) =>
            logger.warn(s"Write failed for some stations (Future failed) msg=$msg args=$args", t)
            reply(ERROR_SUBSCRIPTIONS_COULD_NOT_BE_ADDED)
        }
      }
  }

  protected def buildWxMessage(stations: List[String],
                     metars: Map[String, Seq[METAR]],
                     tafs: Map[String, Seq[TAF]]): String = {
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

  protected def formatDateTime(zonedDateTime: ZonedDateTime) = {

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
