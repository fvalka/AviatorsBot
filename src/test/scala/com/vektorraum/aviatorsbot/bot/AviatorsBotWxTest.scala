package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.service.weather.fixtures.{METARResponseFixtures, TAFResponseFixtures}
import info.mukel.telegrambot4s.methods.ParseMode
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers._
import org.scalatest.concurrent._
import org.scalatest.time.{Millis, Span}
import org.scalatest.{FeatureSpec, GivenWhenThen}

import scala.concurrent.Future

/**
  * Created by fvalka on 21.05.2017.
  */
class AviatorsBotWxTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to")
  info("Be able to retrieve up to date METAR and TAF weather information")
  info("Enhanced with mark up for faster recognition of the current flight conditions and trend")

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(1000, Millis)), interval = scaled(Span(60, Millis)))

  feature("Fetch current METAR and TAF information") {
    scenario("Pilot requests weather for a valid station which has METARs available") {
      Given("AviatorsBotForTesting with valid xml")

      val bot = new AviatorsBotForTesting()

      bot.weatherService.getMetars _ expects where {
        stations: List[String] => stations.head.toUpperCase == "LOWW" && stations.length == 1
      } returns Future.successful { METARResponseFixtures.ValidLOWW7Hours }

      bot.weatherService.getTafs _ expects where {
        stations: List[String] => stations.head.toUpperCase == "LOWW" && stations.length == 1
      } returns Future.successful { TAFResponseFixtures.ValidLOWW }

      When("Requesting weather for a valid station")
      bot.receiveMockMessage("wx loww")

      Then("Correctly formated weather is returned")
      eventually {
        val expected = "<strong>LOWW</strong> ✅ 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 NOSIG\n" +
          "<strong>TAF LOWW</strong> 231715Z 2318/2424 18004KT CAVOK TX22/2318Z TN12/2500Z FM240300 " +
          "29015G25KT 9999 BKN040 TEMPO 2403/2408 30018G30KT 6000 SHRA FEW030 FEW030CB BKN040 PROB30 2404/2407 " +
          "4000 TSRA FM241000 32015G25KT CAVOK TEMPO 2410/2416 33022G32KT BECMG 2416/2418 34012KT"
        bot.replySent shouldEqual expected
        bot.parseMode shouldEqual Some(ParseMode.HTML)
      }
    }

    scenario("Pilot uses /wx instead of wx for retrieving the weather") {
      Given("AviatorsBotForTesting with valid xml")
      val bot = new AviatorsBotForTesting()

      bot.weatherService.getMetars _ expects where {
        stations: List[String] => stations.head == "LOWW" && stations.length == 1
      } returns Future.successful { METARResponseFixtures.ValidLOWW7Hours }

      bot.weatherService.getTafs _ expects where {
        stations: List[String] => stations.head == "LOWW" && stations.length == 1
      } returns Future.successful { TAFResponseFixtures.ValidLOWW }


      When("Requesting weather with /wx <station> instead of wx <station>")
      bot.receiveMockMessage("/wx loww")

      Then("Weather is returned")
      eventually {
        bot.replySent should include ("<strong>LOWW</strong>")
        bot.parseMode shouldEqual Some(ParseMode.HTML)
      }
    }

    scenario("Pilot requests weather for a station which doesn't exist") {
      Given("Aviatorsbot with empty response from weather service")
      val bot = new AviatorsBotForTesting()

      bot.weatherService.getMetars _ expects where {
        stations: List[String] => stations.head.toUpperCase == "LOWP" && stations.length == 1
      } returns Future.successful { Map()}

      bot.weatherService.getTafs _ expects where {
        stations: List[String] => stations.head.toUpperCase == "LOWP" && stations.length == 1
      } returns Future.successful { Map()}


      When("Pilot requests the current weather")
      bot.receiveMockMessage("wx lowp")

      Then("Returns error message that weather could not be retrieved for this station")
      eventually {
        bot.replySent should include("<strong>LOWP</strong> No METAR received for station")
        bot.parseMode shouldEqual Some(ParseMode.HTML)
      }
    }

    scenario("Pilot requests weather with an invalid station name") {
      Given("Aviatorsbot with empty response from weather service")
      val bot = new AviatorsBotForTesting()

      When("Pilot uses an invalid station name")
      bot.receiveMockMessage("wx lowpk")

      Then("Returns error message that the station name is incorrect")
      eventually {
        bot.replySent should include("<strong>usage:</strong> /wx &lt;stations&gt;\nGet the current METAR and TAF for" +
          " these stations\n\n<strong>Examples:</strong>\n/wx loww eddm ... METAR and TAF for these airfields")

        bot.parseMode shouldEqual Some(ParseMode.HTML)
      }
    }

    scenario("Pilot requests weather but an exception occurs in the weather service") {
      Given("Weather service which returns a failed future")
      val bot = new AviatorsBotForTesting()

      bot.weatherService.getMetars _ expects where {
        stations: List[String] => stations.head.toUpperCase == "LOWW" && stations.length == 1
      } returns Future.failed(new RuntimeException())

      bot.weatherService.getTafs _ expects where {
        stations: List[String] => stations.head.toUpperCase == "LOWW" && stations.length == 1
      } returns Future.successful { TAFResponseFixtures.ValidLOWW }

      When("Pilot requests the weather")
      bot.receiveMockMessage("wx loww")

      Then("Returns error message that the weather could not be retrieved")
      eventually {
        bot.replySent should include("Could not retrieve weather")
      }
    }
  }
}
