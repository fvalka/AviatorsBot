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
      val bot = new AviatorsBotForTesting(ResponseFixtures.ValidLOWW7Hours)

      When("Requesting weather for a valid station")
      bot.receiveMockMessage("wx loww")

      Then("Correctly formated weather is returned")
      eventually {
        bot.replySent shouldEqual "<strong>LOWW</strong> ✅ 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 NOSIG"
      }
    }
  }

}
