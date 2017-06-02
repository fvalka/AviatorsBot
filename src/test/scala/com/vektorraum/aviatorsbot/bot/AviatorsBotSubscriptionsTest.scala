package com.vektorraum.aviatorsbot.bot

import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.service.weather.fixtures.{AirfieldFixtures, METARResponseFixtures, TAFResponseFixtures}
import com.vektorraum.aviatorsbot.service.weather.mocks.AddsWeatherServiceForTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers._
import org.scalatest.concurrent._
import org.scalatest.time.{Millis, Span}
import org.scalatest.{FeatureSpec, GivenWhenThen}
import reactivemongo.api.commands.DefaultWriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by fvalka on 21.05.2017.
  */
class AviatorsBotSubscriptionsTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to")
  info("be able to receive periodic weather updates")
  info("with a simple to use and robust user interface")

  object Fixtures {

    val WriteResultOk = DefaultWriteResult(ok = true, 1, List(), None, None, None)
  }

  implicit override val patienceConfig =
    PatienceConfig(timeout = scaled(Span(500, Millis)), interval = scaled(Span(30, Millis)))

  feature("Add subscriptions") {
    scenario("Pilot subscribes to a single station") {
      def checkDate(date: Date): Boolean = {
        val convDate = ZonedDateTime.ofInstant(date.toInstant, ZoneOffset.UTC)
        convDate.isBefore(ZonedDateTime.now().plusHours(7)) && convDate.isAfter(ZonedDateTime.now().plusHours(5))
      }

      Given("AviatorsBotForTesting with valid metar")
      val weatherService = new AddsWeatherServiceForTest(METARResponseFixtures.ValidLOWW7Hours,
        TAFResponseFixtures.ValidLOWW)
      val bot = new AviatorsBotForTesting(weatherService)

      bot.subscriptionDAO.addOrUpdate _ expects where {
        (subscription: Subscription) => subscription.icao == "LOWW" && subscription.metar && subscription.taf &&
          subscription.latestMetar.isEmpty && subscription.latestTaf.isEmpty// && checkDate(subscription.validUntil)
      } returns Future { Fixtures.WriteResultOk }

      When("Adding subscription for single station")
      bot.receiveMockMessage("add loww")

      Then("Correct message is returned and mock is called")
      eventually {
        bot.replySent should startWith ("Subscription is active until:")
      }
    }

    scenario("Pilot enters an invalid ICAO station") {
      Given("AviatorsbotForTesting with valid backend services")
    }
  }
}
