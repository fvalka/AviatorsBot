package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.service.weather.AddsWeatherService
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.Implicits._
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import com.softwaremill.macwire._

import scala.io.Source

/**
  * Created by fvalka on 18.05.2017.
  */
object AviatorsBot extends TelegramBot with Polling with Commands {
  val WelcomeMessage = "Welcome to vektorraum AviatorsBot!\n\n" +
    //"First of all I have to make sure that you read the following legalese:\n"
    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n\n" +
    "THIS SOFTWARE IS NOT AN OFFICIAL BRIEFING SOURCE. ANY DATA SENT MIGHT BE WRONG, OUT OF DATE OR OTHERWISE UNUSABLE OR MISLEADING, NO GUARANTEES CAN BE MADE ABOUT THE AVAILABILITY OF THIS SERVICE, ESPECIALLY THE POLLING/SUBSCRIPTION MECHANISM\n" +
    "USE PURELY AT YOUR OWN RISK!"

  lazy val token: String = scala.util.Properties
    .envOrNone("BOT_TOKEN")
    .getOrElse(Source.fromResource("bot.token").getLines().mkString)

  lazy val weatherService = wire[AddsWeatherService]

  on("hello") { implicit msg => _ => reply("My token is SAFE!") }

  on("/start") { implicit msg => _ => reply(WelcomeMessage) }

  on("wx", "METAR for multiple stations") { implicit msg => args =>
    weatherService.getMetars()
  }

}
