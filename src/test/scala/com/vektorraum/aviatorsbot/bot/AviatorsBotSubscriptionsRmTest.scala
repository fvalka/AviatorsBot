package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.persistence.WriteResultFixtures
import com.vektorraum.aviatorsbot.persistence.subscriptions.fixtures.SubscriptionFixtures
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers._
import org.scalatest.concurrent._
import org.scalatest.time.{Millis, Span}
import org.scalatest.{FeatureSpec, GivenWhenThen}

import scala.concurrent.Future

class AviatorsBotSubscriptionsRmTest extends FeatureSpec
  with GivenWhenThen with MockFactory with Eventually with SubscriptionFixtures {
  info("As a pilot I want to")
  info("be able to no longer receive weather updates by ")
  info("removing my subscriptions from the bot")

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(2000, Millis)), interval = scaled(Span(30, Millis)))

  protected val ERROR_MESSAGE = "Could not unsubscribe from all stations. Please try again!"
  feature("Remove subscriptions") {
    scenario("Pilot wants to remove a subscription which exists") {
      Given("AviatorsBot with mock for removing the subscription")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.remove _ expects where {
        (chatId: Long, station: String) => {
          chatId == bot.testChatId && station == "LOWW"
        }
      } returns Future.successful(WriteResultFixtures.WriteResultOk)

      When("Pilot sends the rm command for the correct station")
      bot.receiveMockMessage("/rm loww")

      Then("Pilot receives the deletion confirmation")
      eventually {
        bot.replySent shouldEqual "Unsubscribed successfully"
      }
    }

    scenario("Pilot wants to remove all stations with wildcard") {
      Given("AviatorsBot with mock for removing the subscription")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.remove _ expects where {
        (chatId: Long, station: String) => {
          chatId == bot.testChatId && station == "*"
        }
      } returns Future.successful(WriteResultFixtures.WriteResultOk)

      When("Pilot sends the rm command with wildcard")
      bot.receiveMockMessage("/rm *")

      Then("Pilot receives the deletion confirmation")
      eventually {
        bot.replySent shouldEqual "Unsubscribed successfully"
      }
    }

    scenario("A database error occurs, failed Future") {
      Given("AviatorsBot with mock returning a failed future")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.remove _ expects where {
        (chatId: Long, station: String) =>
          chatId == bot.testChatId && station == "LOWW"
      } returns Future.failed(new RuntimeException())

      When("Pilot sends the rm command")
      bot.receiveMockMessage("/rm LOWW")

      Then("Pilot receives an error message")
      eventually {
        bot.replySent shouldEqual ERROR_MESSAGE
      }
    }

    scenario("Failed WriteResult from the DAO") {
      Given("AviatorsBot with mock returning failed write")
      val bot = new AviatorsBotForTesting

      bot.subscriptionDAO.remove _ expects where {
        (chatId: Long, station: String) =>
          chatId == bot.testChatId && station == "LOWW"
      } returns Future.successful(WriteResultFixtures.WriteResultFailed)

      When("Pilot sends the rm command")
      bot.receiveMockMessage("rm loww")

      Then("Pilot receives an error message")
      eventually {
        bot.replySent shouldEqual ERROR_MESSAGE
      }
    }

    scenario("Incorrect input returns the help message") {
      Given("AviatorsBot without mock")
      val bot = new AviatorsBotForTesting()

      When("Pilot sends the rm command")
      bot.receiveMockMessage("/rm")

      Then("Pilot receives the help message")
      eventually {
        bot.replySent should include ("usage")
      }
    }
  }
}
