package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.service.weather.fixtures.ResponseFixtures
import com.vektorraum.aviatorsbot.service.weather.mocks.AddsWeatherServiceForTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FeatureSpec, GivenWhenThen}
import org.scalatest.Matchers._
import org.scalatest.concurrent._
import org.scalatest.time.{Millis, Seconds, Span}

/**
  * Created by fvalka on 21.05.2017.
  */
class AviatorsBotTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to")
  info("Be able to retrieve up to date METAR and TAF weather information")
  info("Enhanced with mark up for faster recognition of the current flight conditions and trend")

  implicit override val patienceConfig =
    PatienceConfig(timeout = scaled(Span(200, Millis)), interval = scaled(Span(30, Millis)))

  feature("Fetch current METAR and TAF information") {
    scenario("Pilot requests weather for a valid station which has METARs available") {
      Given("AviatorsBotForTesting with valid xml")
      val weatherService = new AddsWeatherServiceForTest(ResponseFixtures.ValidLOWW7Hours)
      val bot = new AviatorsBotForTesting(weatherService)

      When("Requesting weather for a valid station")
      bot.receiveMockMessage("wx loww")

      Then("Correctly formated weather is returned")
      eventually {
        bot.replySent shouldEqual "<strong>LOWW</strong> ✅ 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 NOSIG"
      }
    }

    scenario("Pilot requests weather for a station which doesn't exist") {
      Given("Aviatorsbot with empty response from weather service")
      val weatherService = new AddsWeatherServiceForTest(ResponseFixtures.EmptyResponseForStationWhichDoesntExist)
      val bot = new AviatorsBotForTesting(weatherService)

      When("Pilot requests the current weather")
      bot.receiveMockMessage("wx lowp")

      Then("Returns error message that weather could not be retrieved for this station")
      eventually(
        bot.replySent shouldEqual "<strong>LOWP</strong> No METAR received for station"
      )
    }

    scenario("Pilot requests weather with an invalid station name") {
      Given("Aviatorsbot with empty response from weather service")
      val weatherService = new AddsWeatherServiceForTest(ResponseFixtures.EmptyResponseForStationWhichDoesntExist)
      val bot = new AviatorsBotForTesting(weatherService)

      When("Pilot uses an invalid station name")
      bot.receiveMockMessage("wx lowpk")

      Then("Returns error message that the station name is incorrect")
      eventually(
        bot.replySent shouldEqual "Please provide a valid ICAO station or list of stations e.g. \"wx LOWW LOAV\""
      )
    }
  }
}
