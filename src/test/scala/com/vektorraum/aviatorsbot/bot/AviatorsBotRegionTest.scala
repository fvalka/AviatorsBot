package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.persistence.WriteResultFixtures
import com.vektorraum.aviatorsbot.persistence.regions.model.RegionSetting
import com.vektorraum.aviatorsbot.service.regions.Regions
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{AsyncFeatureSpec, GivenWhenThen}
import org.scalatest.Matchers._

import scala.concurrent.Future

class AviatorsBotRegionTest extends AsyncFeatureSpec with GivenWhenThen with AsyncMockFactory {
  info("As a pilot I want to")
  info("receive weather charts and other information in my region")
  info("without having to input the region whenever I call a command")

  feature("The current region can be viewed by the user and all available regions listed") {
    scenario("No region is stored for the pilot in the database, default used") {
      Given("AviatorsBotForTesting")
      val bot = new AviatorsBotForTesting
      bot.regionsDAO.get _ expects bot.testChatId returning Future.successful(None)

      When("Requesting the list of stations")
      bot.receiveMockMessage("/region")

      Then("Correct regions are returned and current region is default")
      bot.replyFuture map { reply =>
        reply should include("Default: Europe")
        reply should include("Available regions")
        reply should include("eu")
        reply should include("Europe")
        reply should include("as")
        reply should include("Asia")
        reply should include("na")
        reply should include("North America")
      }
    }

    scenario("A region is stored in the database for this user") {
      Given("AviatorsBot for testing with mock containing subscription")
      val bot = new AviatorsBotForTesting
      bot.regionsDAO.get _ expects bot.testChatId returning
        Future.successful(Some(RegionSetting(bot.testChatId, Regions.Africa)))

      When("Requesting the current region")
      bot.receiveMockMessage("/region")

      Then("The actual region stored in the database is returned")
      bot.replyFuture map { reply =>
        reply should include("<strong>Current region:</strong> Africa")
      }
    }

    scenario("Read from database fails") {
      Given("AviatorsBot for testing with mock containing subscription")
      val bot = new AviatorsBotForTesting
      bot.regionsDAO.get _ expects bot.testChatId returning
        Future.failed(new RuntimeException)

      When("Requesting the current region")
      bot.receiveMockMessage("/region")

      Then("The actual region stored in the database is returned")
      bot.replyFuture map { reply =>
        reply should include("Could not get current region from the database")
      }
    }
  }

  feature("Change region") {
    scenario("User sends a valid region short code") {
      Given("AviatorsBot for testing with mock expecting a region write")
      val bot = new AviatorsBotForTesting
      bot.regionsDAO.set _ expects RegionSetting(bot.testChatId, Regions.Asia) returning
        Future.successful(WriteResultFixtures.WriteResultOk)

      When("User changes the region to Asia using the short code")
      bot.receiveMockMessage("/region as")

      Then("User receives a confirmation message")
      bot.replyFuture map { reply =>
        reply should include("region updated")
      }
    }

    scenario("Writing the region fails with writeresult not okay") {
      Given("AviatorsBot for testing with mock expecting a region write")
      val bot = new AviatorsBotForTesting
      bot.regionsDAO.set _ expects RegionSetting(bot.testChatId, Regions.Asia) returning
        Future.successful(WriteResultFixtures.WriteResultFailed)

      When("User changes the region to Asia using the short code")
      bot.receiveMockMessage("/region as")

      Then("User receives an error message")
      bot.replyFuture map { reply =>
        reply should include("Could not store preference")
      }
    }

    scenario("Writing the region fails because of a failed future") {
      Given("AviatorsBot for testing with mock expecting a region write")
      val bot = new AviatorsBotForTesting
      bot.regionsDAO.set _ expects RegionSetting(bot.testChatId, Regions.Asia) returning
        Future.failed(new RuntimeException)

      When("User changes the region to Asia using the short code")
      bot.receiveMockMessage("/region as")

      Then("User receives an error message")
      bot.replyFuture map { reply =>
        reply should include("Could not store preference")
      }
    }
  }

}
