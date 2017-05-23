package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.service.weather.{AddsWeatherService, AddsWeatherServiceProduction}
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import com.softwaremill.macwire._
import com.typesafe.scalalogging.Logger
import com.vektorraum.aviatorsbot.bot.util.{AliasCommands, StationUtil}
import com.vektorraum.aviatorsbot.bot.weather.{FormatMetar, FormatTaf}
import com.vektorraum.aviatorsbot.generated.metar.METAR
import com.vektorraum.aviatorsbot.generated.taf.TAF
import info.mukel.telegrambot4s.methods.ParseMode.ParseMode

import scala.collection.mutable
import scala.concurrent.Future
import scala.io.Source
import scala.util.{Failure, Success}

/**
  * Created by fvalka on 18.05.2017.
  */
trait AviatorsBot extends TelegramBot with Polling with AliasCommands {
  protected val trafficLog = Logger("traffic-log")

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
    .getOrElse(Source.fromResource("bot.token").getLines().mkString)

  protected lazy val weatherService: AddsWeatherService = wire[AddsWeatherServiceProduction]

  on("hello") { implicit msg => _ => reply("My token is SAFE!") }

  on("/start") { implicit msg => _ => reply(WelcomeMessage) }

  on("wx", "METAR for multiple stations") { implicit msg => args =>
    if(!args.forall(arg => StationUtil.isValidInput(arg))) {
      reply("Please provide a valid ICAO station or list of stations e.g. \"wx LOWW LOAV\"")
    } else {
      val stations = args.toList.map(station => station.toUpperCase())
      val message = for {
        metars <- weatherService.getMetars(stations)
        tafs <- weatherService.getTafs(stations)
      } yield { buildWxMessage(stations, metars, tafs) }

      message onComplete {
        case Success(m) => reply(m, parseMode = ParseMode.HTML)
        case Failure(t) => reply("Could not retrieve METARs")
      }
    }
  }

  def buildWxMessage(stations: List[String],
                     metars: Map[String, Seq[METAR]],
                     tafs: Map[String, Seq[TAF]]): String = {
    val inputStationsSet = mutable.LinkedHashSet(stations.filter(StationUtil.isActualStation): _*)
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
