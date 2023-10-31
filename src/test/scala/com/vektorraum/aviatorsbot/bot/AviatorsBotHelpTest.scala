package com.vektorraum.aviatorsbot.bot

import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.matchers.should.Matchers._
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AsyncFeatureSpec

class AviatorsBotHelpTest extends AsyncFeatureSpec with GivenWhenThen with AsyncMockFactory {
  info("As a pilot I want to")
  info("Be able to see which commands I can use on this bot")

  feature("Help command shows all available commands") {
    scenario("Pilot sends just the help command") {
      Given("AviatorsBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot sends the help command")
      bot.receiveMockMessage("/help")

      Then("Help message is received")
      bot.replyFuture.map { res =>
        res should include ("/rm")
        res should include ("/ls")
      }
    }

    scenario("Help command works with any arguments") {
      Given("AviatorsBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot sends the help command")
      bot.receiveMockMessage("/help asd 949 xjsjsj")

      Then("Help message is received")
      bot.replyFuture.map { res =>
        res should include ("/rm")
        res should include ("/ls")
      }
    }

    scenario("Help command works without prefix in front") {
      Given("AviatorsBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot sends the help command")
      bot.receiveMockMessage("help")

      Then("Help message is received")
      bot.replyFuture.map { res =>
        res should include ("/rm")
        res should include ("/ls")
      }
    }

    scenario("Help also returns help on other commands") {
      Given("AviatorsBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot request help on add command")
      bot.receiveMockMessage("/help add")

      Then("Usage information for this command is returned")
      bot.replyFuture.map { res =>
        res should include ("/add")
        res should include ("usage")
      }
    }
  }

  feature("Unknown command input leads to the help message") {
    scenario("Pilot sends non existent command") {
      Given("AviatorsBot without any mocks")
      val bot = new AviatorsBotForTesting()

      When("Pilot sends a non existent command")
      bot.receiveMockMessage("/thiswillneverexist")

      Then("Help message is received")
      bot.replyFuture.map { res =>
        res should include ("/rm")
        res should include ("/ls")
      }
    }
  }

}
