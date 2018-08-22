package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.service.weather.fixtures.{METARFixtures, METARResponseFixtures, TAFResponseFixtures}
import info.mukel.telegrambot4s.methods.ParseMode
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FeatureSpec, GivenWhenThen}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Millis, Span}
import org.scalatest.Matchers._

import scala.concurrent.Future

class AviatorsBotDaTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to")
  info("Be able to calculate the density altitude")
  info("Without having to provide any additional input, besides the station name")

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(1000, Millis)), interval = scaled(Span(60, Millis)))

  feature("Density altitude calculation based only upon the METAR") {
    scenario("Pilot calculates da for station which has a valid and complete METAR") {
      Given("AviatorsBotForTesting with mocks for the weather service")
      val bot = new AviatorsBotForTesting()

      bot.weatherService.getMetars _ expects where {
        stations: Iterable[String] => stations.head.toUpperCase == "LOWW"
      } returns Future.successful { METARResponseFixtures.ValidLOWW7Hours }

      When("Requesting density altitude for valid station")
      bot.receiveMockMessage("da loww")

      Then("The correct result is returned")
      eventually {
        val expected = "METAR observation time: 2017-5-21 1150Z\n" +
          "Elevation: 623 ft\n" +
          "Density altitude: <strong>1103 ft</strong>"
        bot.replySent shouldEqual expected
        bot.parseMode shouldEqual Some(ParseMode.HTML)
      }
    }

    scenario("If the command is called without a station a help message is sent") {
      Given("AviatorsBotForTesting")
      val bot = new AviatorsBotForTesting()

      When("Requesting density altitude for valid station")
      bot.receiveMockMessage("/da")

      Then("A help message is returned")
      eventually {
        bot.replySent should include ("usage")
      }
    }

    scenario("Metar with missing fields lead to correct error message") {
      Given("AviatorsBotForTesting")
      val bot = new AviatorsBotForTesting()

      val metars = Map("LOWW" -> List(METARFixtures.ValidAndCompleteLOWW.copy(dewpoint_c = None)))

      bot.weatherService.getMetars _ expects where {
        stations: Iterable[String] => stations.head.toUpperCase == "LOWW"
      } returns Future.successful { metars }

      When("Requesting density altitude for valid station")
      bot.receiveMockMessage("da loww")

      Then("An error message is returned")
      eventually {
        bot.replySent shouldEqual "Missing values in METAR, calculation can not be performed."
      }
    }

    scenario("Empty weather service result leads to correct error message") {
      Given("AviatorsBotForTesting")
      val bot = new AviatorsBotForTesting()

      bot.weatherService.getMetars _ expects where {
        stations: Iterable[String] => stations.head.toUpperCase == "LOWW"
      } returns Future.successful { Map.empty }

      When("Requesting density altitude for valid station")
      bot.receiveMockMessage("da loww")

      Then("An error message is returned")
      eventually {
        bot.replySent shouldEqual "Could not retrieve weather for station"
      }
    }

    scenario("Weather service fails") {
      Given("AviatorsBotForTesting")
      val bot = new AviatorsBotForTesting()

      bot.weatherService.getMetars _ expects where {
        stations: Iterable[String] => stations.head.toUpperCase == "LOWW"
      } returns Future.failed(new RuntimeException)

      When("Requesting density altitude for valid station")
      bot.receiveMockMessage("da loww")

      Then("An error message is returned")
      eventually {
        bot.replySent shouldEqual "Could not perform density altitude calculation for this station"
      }
    }
  }

}
