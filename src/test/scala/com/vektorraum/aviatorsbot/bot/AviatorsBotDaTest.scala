package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.service.weather.fixtures.{METARFixtures, METARResponseFixtures}
import info.mukel.telegrambot4s.methods.ParseMode
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.Matchers._
import org.scalatest.{AsyncFeatureSpec, GivenWhenThen}

import scala.concurrent.Future

class AviatorsBotDaTest extends AsyncFeatureSpec with GivenWhenThen with AsyncMockFactory {
  info("As a pilot I want to")
  info("Be able to calculate the density altitude")
  info("Without having to provide any additional input, besides the station name")

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
      for {
        result <- bot.replyFuture
        parseMode <- bot.parseModeFuture
      } yield {
        val expected = "METAR observation time: 2017-5-21 1150Z\n" +
          "Elevation: 623 ft\n" +
          "Density altitude: <strong>1103 ft</strong>"
        result shouldEqual expected
        parseMode shouldEqual Some(ParseMode.HTML)
      }
    }

    scenario("If the command is called without a station a help message is sent") {
      Given("AviatorsBotForTesting")
      val bot = new AviatorsBotForTesting()

      When("Requesting density altitude for valid station")
      bot.receiveMockMessage("/da")

      Then("A help message is returned")
      bot.replyFuture map { result =>
        result should include ("usage")
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
      bot.replyFuture map { result =>
        result shouldEqual "Missing values in METAR, calculation can not be performed."
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
      bot.replyFuture map { result =>
        result shouldEqual "Could not retrieve weather for station"
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
      bot.replyFuture map { result =>
        result shouldEqual "Could not perform density altitude calculation for this station"
      }
    }

    scenario("Metar with missing fields lead to nearby station") {
      Given("AviatorsBotForTesting")
      val bot = new AviatorsBotForTesting()

      val metars = Map("LOWW" -> List(METARFixtures.ValidAndCompleteLOWW.copy(altim_in_hg = None)))

      bot.weatherService.getMetars _ expects where {
        stations: Iterable[String] => stations.head.toUpperCase == "LOWW"
      } returns Future.successful { metars }

      bot.airfieldDAO.near _ expects ("LOWW", *) returning Future.failed(new RuntimeException)

      When("Requesting density altitude for valid station")
      bot.receiveMockMessage("da loww")

      Then("An error message is returned")
      bot.replyFuture map { result =>
        result shouldEqual "Could not perform density altitude calculation for this station"
      }
    }
  }

}
