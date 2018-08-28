package com.vektorraum.aviatorsbot.bot

import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers._
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Millis, Span}
import org.scalatest.{Assertion, FeatureSpec, GivenWhenThen}

class AviatorsBotInfoMessagesTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to be able to ")
  info("get information about the bot")

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(1000, Millis)), interval = scaled(Span(60, Millis)))

  feature("Start command sends information about the bot") {
    scenario("Pilot sends normal start command") {
      Given("AviatorBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot starts the interaction with /start")
      bot.receiveMockMessage("/start")

      Then("Pilot receives information about the bot usage")
      eventually {
        checkStartMessage(bot)
      }
    }

    scenario("Pilot sends start command with arguments and still receives the correct message") {
      Given("AviatorBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot starts the interaction incorrectly, with arguments to /start")
      bot.receiveMockMessage("/start bla")

      Then("Pilot still receives information about the bot usage")
      eventually {
        checkStartMessage(bot)
      }
    }
  }

  private def checkStartMessage(bot: AviatorsBotForTesting): Assertion = {
    bot.replySent.toLowerCase should include("welcome")
    bot.replySent should include("/privacy")
    bot.replySent should include("/help")
    bot.replySent.toLowerCase should include("liability")
  }

  feature("Privacy policy is sent to the user upon request") {
    scenario("Privacy policy is requested correctly without any arguments") {
      Given("AviatorBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot requests privacy notice")
      bot.receiveMockMessage("/privacy")

      Then("Reply contains a privacy policy")
      eventually {
        checkPrivacyPolicy(bot)
      }
    }

    scenario("Privacy policy is requested incorrectly with arguments") {
      Given("AviatorBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot requests privacy notice incorrectly")
      bot.receiveMockMessage("/privacy LOWW")

      Then("Reply contains a privacy policy")
      eventually {
        checkPrivacyPolicy(bot)
      }
    }
  }

  private def checkPrivacyPolicy(bot: AviatorsBotForTesting): Assertion = {
    bot.replySent.toLowerCase should include("privacy policy")
    bot.replySent.toLowerCase should include("data")
    bot.replySent.toLowerCase should include("contact")
  }
}
