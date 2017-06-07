package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.persistence.airfielddata.AirfieldDAO
import com.vektorraum.aviatorsbot.service.weather.fixtures.{AirfieldFixtures, METARResponseFixtures, TAFResponseFixtures}
import com.vektorraum.aviatorsbot.service.weather.mocks.AddsWeatherServiceForTest
import info.mukel.telegrambot4s.methods.ParseMode
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers._
import org.scalatest.concurrent._
import org.scalatest.time.{Millis, Span}
import org.scalatest.{FeatureSpec, GivenWhenThen}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by fvalka on 21.05.2017.
  */
class AviatorsBotXWindTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to")
  info("be able to calculate the crosswind for an airfield")
  info("without having to manually copy all the weather information into an additional calculation program")

  implicit override val patienceConfig =
    PatienceConfig(timeout = scaled(Span(500, Millis)), interval = scaled(Span(30, Millis)))

  feature("Calculate the crosswind for an airfield based upon the current METAR") {
    scenario("Pilot requests crosswind calculation for a valid airfield which has a current METAR available") {
      Given("AviatorsBotForTesting with valid metar and airfield from mock db")
      val weatherService = new AddsWeatherServiceForTest(METARResponseFixtures.ValidLOWW7Hours,
        TAFResponseFixtures.ValidLOWW)
      val bot = new AviatorsBotForTesting(weatherService)
      bot.airfieldDAO.findByIcao _ when "LOWW" returns Future {Some(AirfieldFixtures.LOWW)}

      When("Requesting crosswind for same station")
      bot.receiveMockMessage("xwind loww")

      Then("Correctly calculated crosswind is returned")
      eventually {
        bot.replySent shouldEqual "METAR issued at: 2017-05-21T11:50:00Z\n" +
          "<strong>11</strong> ➡4KT ⬆17KT\n" +
          "<strong>16</strong> ⬅10KT ⬆15KT\n" +
          "<strong>29</strong> ⬅4KT ⬇17KT\n" +
          "<strong>34</strong> ➡10KT ⬇15KT"
        bot.parseMode shouldEqual Some(ParseMode.HTML)
      }
    }

    scenario("Pilot request xwind for a station which is not in the database") {
      Given("AviatorsBotForTesting with valid metar and no database entry for station")
      val weatherService = new AddsWeatherServiceForTest(METARResponseFixtures.ValidLOWW7Hours,
        TAFResponseFixtures.ValidLOWW)
      val bot = new AviatorsBotForTesting(weatherService)
      bot.airfieldDAO.findByIcao _ when "LNPP" returns Future {None}

      When("Requesting xwind for station which is not in the database")
      bot.receiveMockMessage("xwind lnpp")

      Then("Error message is returned")
      eventually {
        bot.replySent shouldEqual "Airfield not found"
      }
    }

    scenario("Pilot requests xwind for station for which no METAR can be retrieved") {
      Given("AviatorsBotForTesting with valid metar and no database entry for station")
      val weatherService = new AddsWeatherServiceForTest(METARResponseFixtures.EmptyResponseForStationWhichDoesntExist,
        TAFResponseFixtures.ValidLOWW)
      val bot = new AviatorsBotForTesting(weatherService)
      bot.airfieldDAO.findByIcao _ when "LOWW" returns Future {Some(AirfieldFixtures.LOWW)}

      When("Requesting xwind for station which is not in the database")
      bot.receiveMockMessage("xwind loww")

      Then("Error message is returned")
      eventually {
        bot.replySent shouldEqual "Could not retrieve weather for station"
      }
    }

    scenario("Pilot requests xwind with an invalid identifier") {
      Given("AviatorsBotForTesting with valid metar and valid database entry")
      val weatherService = new AddsWeatherServiceForTest(METARResponseFixtures.ValidLOWW7Hours,
        TAFResponseFixtures.ValidLOWW)
      val bot = new AviatorsBotForTesting(weatherService)
      bot.airfieldDAO.findByIcao _ when "LOWW" returns Future {Some(AirfieldFixtures.LOWW)}

      When("Requesting xwind for invalid station identifier")
      bot.receiveMockMessage("xwind lowkw")

      Then("Error message is returned")
      eventually {
        bot.replySent shouldEqual "<strong>usage:</strong> /xwind &lt;station&gt;\nCalculate the crosswind for all " +
          "runways of the airfield based upon the current METAR\n\n<strong>Example:</strong>\n/xwind LOWW ... " +
          "Crosswind for LOWW"
        bot.parseMode shouldEqual Some(ParseMode.HTML)
      }
    }

    scenario("Pilot requests xwind and an exception occurs") {
      Given("AviatorsBotForTesting with valid metar and DAO throws an exception")
      val weatherService = new AddsWeatherServiceForTest(METARResponseFixtures.ValidLOWW7Hours,
        TAFResponseFixtures.ValidLOWW)
      val bot = new AviatorsBotForTesting(weatherService)
      bot.airfieldDAO.findByIcao _ when "LOWW" returns Future.failed(new RuntimeException())

      When("Requesting xwind")
      bot.receiveMockMessage("xwind loww")

      Then("Error message is returned")
      eventually {
        bot.replySent shouldEqual "Could not perform crosswind calculation for this station"
      }
    }
  }
}
