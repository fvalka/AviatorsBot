package com.vektorraum.aviatorsbot.bot

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FeatureSpec, GivenWhenThen}
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Millis, Span}
import org.scalatest.Matchers._

class AviatorsBotHelpTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to")
  info("Be able to see which commands I can use on this bot")

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(1000, Millis)), interval = scaled(Span(60, Millis)))

  feature("Help command shows all available commands") {
    scenario("Pilot sends just the help command") {
      Given("AviatorsBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot sends the help command")
      bot.receiveMockMessage("/help")

      Then("Help message is received")
      eventually {
        bot.replySent should include ("/rm")
        bot.replySent should include ("/ls")
      }
    }

    scenario("Help command works with any arguments") {
      Given("AviatorsBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot sends the help command")
      bot.receiveMockMessage("/help asd 949 xjsjsj")

      Then("Help message is received")
      eventually {
        bot.replySent should include ("/rm")
        bot.replySent should include ("/ls")
      }
    }

    scenario("Help command works without prefix in front") {
      Given("AviatorsBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot sends the help command")
      bot.receiveMockMessage("help")

      Then("Help message is received")
      eventually {
        bot.replySent should include ("/rm")
        bot.replySent should include ("/ls")
      }
    }

    scenario("Help also returns help on other commands") {
      Given("AviatorsBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot request help on add command")
      bot.receiveMockMessage("/help add")

      Then("Usage information for this command is returned")
      eventually {
        bot.replySent should include ("/add")
        bot.replySent should include ("usage")
      }
    }
  }

  feature("Unkown command input leads to the help message") {
    scenario("Pilot sends non existant command") {
      Given("AviatorsBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot sends a nonexistant command")
      bot.receiveMockMessage("/thiswillneverexist")

      Then("Help message is received")
      eventually {
        bot.replySent should include ("/rm")
        bot.replySent should include ("/ls")
      }
    }
  }

}
