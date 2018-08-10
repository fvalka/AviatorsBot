package com.vektorraum.aviatorsbot.bot

import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import com.vektorraum.aviatorsbot.persistence.WriteResultFixtures
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
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
class AviatorsBotSubscriptionsAddTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to")
  info("be able to receive periodic weather updates")
  info("by adding subscriptions to the bot")

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(500, Millis)), interval = scaled(Span(30, Millis)))

  val HELP_ADD: String = "<strong>usage:</strong> /add [&lt;valid-until&gt;] [metar] [taf] &lt;stations&gt;" +
    "\nSubscribe to weather updates of these station(s)\n\n<strong>Options:</strong>\n valid-until - UTC time " +
    "in HHmm format or number of hours\n metar - Receive only METAR updates\n taf - Receive only TAF " +
    "updates\n\n<strong>Examples:</strong>\n/add loww eddm ... subscribe to METARs and TAFs using the default " +
    "expiration time of 6 hours\n/add 1507 metar lowg ... subscribe only to METARs of LOWG until 1507Z\n/add 6 " +
    "kjfk ... subscribe to METARS and TAFs of KJFK for 6 hours"


  feature("Add subscriptions") {
    scenario("Pilot subscribes to a single station") {
      def checkDate(date: Date): Boolean = {
        val convDate = ZonedDateTime.ofInstant(date.toInstant, ZoneOffset.UTC)
        convDate.isBefore(ZonedDateTime.now().plusHours(7)) && convDate.isAfter(ZonedDateTime.now().plusHours(5))
      }

      Given("AviatorsBotForTesting")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.addOrExtend _ expects where {
        subscription: Subscription => subscription.icao == "LOWW" && subscription.metar && subscription.taf &&
          subscription.latestMetar.isEmpty && subscription.latestTaf.isEmpty// && checkDate(subscription.validUntil)
      } returns Future { WriteResultFixtures.WriteResultOk }

      When("Adding subscription for single station")
      bot.receiveMockMessage("add loww")

      Then("Correct message is returned and mock is called")
      eventually {
        bot.replySent should startWith ("Subscription is active until:")
      }
    }

    scenario("Pilot enters an invalid ICAO station") {
      Given("AviatorsbotForTesting with valid backend services, expecting no call")
      val bot: AviatorsBotForTesting = initBotForErrorCase

      When("Using invalid input")
      bot.receiveMockMessage("add lowwx")

      Then("Help message is returned to the pilot")
      eventually {
        bot.replySent shouldEqual HELP_ADD
        bot.parseMode shouldEqual Some(ParseMode.HTML)
      }
    }

    scenario("Pilot provides no input") {
      Given("AviatorBotForTesting with valid backend services, expecting no call")
      val bot: AviatorsBotForTesting = initBotForErrorCase

      When("Only sending the command with no input")
      bot.receiveMockMessage("add")

      Then("A help message is returned")
      eventually {
        bot.replySent shouldEqual HELP_ADD
        bot.parseMode shouldEqual Some(ParseMode.HTML)
      }
    }

    scenario("Database writing fails with exception") {
      Given("AviatorsBotForTesting with backend throwing exception")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.addOrExtend _ expects * returns Future{ throw new Exception() }

      When("Trying to add a valid station")
      bot.receiveMockMessage("/add LOWW")

      Then("An error message is returned")
      eventually {
        bot.replySent shouldEqual "Subscriptions could not be stored. Please try again!"
      }
    }

    scenario("Database writing fails with write result failed") {
      Given("AviatorsBotForTesting with backend throwing exception")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.addOrExtend _ expects * returns Future{ WriteResultFixtures.WriteResultFailed }

      When("Trying to add a valid station")
      bot.receiveMockMessage("/add KJFK")

      Then("An error message is returned")
      eventually {
        bot.replySent shouldEqual "Subscriptions could not be stored. Please try again!"
      }
    }
  }

  private def initBotForErrorCase: AviatorsBotForTesting = {
    val bot = new AviatorsBotForTesting()

    (bot.subscriptionDAO.addOrExtend _ expects * returns Future.successful {
      WriteResultFixtures.WriteResultOk
    }).never()

    bot
  }
}
