package com.vektorraum.aviatorsbot.bot

import java.time.{ZoneOffset, ZonedDateTime}

import com.vektorraum.aviatorsbot.bot.util.TimeFormatter
import com.vektorraum.aviatorsbot.persistence.subscriptions.fixtures.SubscriptionFixtures
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent._
import org.scalatest.Matchers._
import org.scalatest.time.{Millis, Span}
import org.scalatest.{FeatureSpec, GivenWhenThen}

import scala.concurrent.Future

class AviatorsBotSubscriptionsLsTest extends FeatureSpec
  with GivenWhenThen with MockFactory with Eventually with SubscriptionFixtures {
  info("As a pilot I want to")
  info("be able to see all my current subscriptions")

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(1000, Millis)), interval = scaled(Span(30, Millis)))

  feature("List subscriptions") {
    scenario("Pilot lists two stored subscriptions") {
      Given("Aviatorsbot with mock returning two valid subscriptions")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.findAllByChatId _ expects bot.testChatId returns
        Future.successful(List(subscription1, subscription2))

      val validUntilFormatted = TimeFormatter.shortUTCDateTimeFormat(
        ZonedDateTime.ofInstant(validUntil.toInstant, ZoneOffset.UTC))

      When("Pilot lists the subscriptions")
      bot.receiveMockMessage("/ls")

      Then("A list of all subscriptions is returned")
      eventually {
        bot.replySent shouldEqual "<strong>ICAO  - valid until</strong>\n" +
          s"<strong>LOWW</strong> - $validUntilFormatted\n" +
          s"<strong>KJFK</strong> - $validUntilFormatted"
      }
    }

    scenario("Pilot lists subscriptions while no subscriptions are stored, getting info message") {
      Given("Aviatorsbot with mock returning an empty list")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.findAllByChatId _ expects bot.testChatId returns Future.successful(List.empty)

      When("Pilot requests a list of subscriptions")
      bot.receiveMockMessage("ls")

      Then("An info message that no subscriptions are stored is sent back to the pilot")
      eventually {
        bot.replySent shouldEqual "No active subscriptions"
      }
    }

    scenario("Pilot lists subscriptions but the database throws an error") {
      Given("AviatorsBot with mock returning a failed Future for the DAO call")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.findAllByChatId _ expects bot.testChatId returns Future.failed(new RuntimeException)

      When("Pilot requests a list of subscriptions")
      bot.receiveMockMessage("/ls")

      Then("An error message is sent to the pilot")
      eventually {
        bot.replySent shouldEqual "Subscriptions could not be listed. Please try again!"
      }
    }

    scenario("Invalid input sent returns the help message ") {
      Given("Aviatorsbot without mock")
      val bot = new AviatorsBotForTesting()

      When("Pilot requests a list of subscriptions with wrong input")
      bot.receiveMockMessage("ls blabla")

      Then("A help message is sent")
      eventually {
        bot.replySent should include ("usage")
      }
    }
  }

}
