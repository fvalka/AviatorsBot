package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.service.regions.Regions
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{AsyncFeatureSpec, GivenWhenThen}
import org.scalatest.Matchers._

class AviatorsBotStrikesTest extends AsyncFeatureSpec with GivenWhenThen with AsyncMockFactory {
  info("As a pilot")
  info("I want to know where there are the current lightning strikes")
  info("by getting a map of currently recorded strikes")

  feature("Strike maps are sent to the user upon request") {
    scenario("Pilot requests strikes without selecting a region") {
      Given("AviatorsBotForTesting with mock for the url service")
      val bot = new AviatorsBotForTesting()
      val testUrl = Some("http://test-eu-url")
      bot.strikesService.getUrl _ expects Regions.Europe returning testUrl

      When("Pilot requests the strike map")
      bot.receiveMockMessage("/strikes")

      Then("The photo is sent to the user")
      bot.photoSentFuture map { res =>
        res shouldEqual testUrl.get
      }
    }

    scenario("Pilot requests strikes for a specific region") {
      Given("AviatorsBotForTesting with mock for the url service")
      val bot = new AviatorsBotForTesting()
      val testUrl = Some("http://test-north-america-url")
      bot.strikesService.getUrl _ expects Regions.NorthAmerica returning testUrl

      When("Pilot requests the strike map")
      bot.receiveMockMessage("/strikes na")

      Then("The photo is sent to the user")
      bot.photoSentFuture map { res =>
        res shouldEqual testUrl.get
      }
    }
  }

  feature("Regions which don't have a map are handled gracefully") {
    scenario("Pilot requests the strikes for a region which is not covered") {
      Given("AviatorsBotForTesting with mock for the url service")
      val bot = new AviatorsBotForTesting()
      bot.strikesService.getUrl _ expects Regions.Africa returning None

      When("Pilot requests strikes for a region which is not covered")
      bot.receiveMockMessage("/strikes af")

      Then("The pilot receives an error message")
      bot.replyFuture map { res =>
        res shouldEqual "No strikes available for this region"
      }
    }
  }

}
