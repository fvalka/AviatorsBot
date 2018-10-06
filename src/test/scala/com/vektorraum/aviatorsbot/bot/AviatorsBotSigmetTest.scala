package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.service.regions.Regions
import com.vektorraum.aviatorsbot.service.sigmets.PlotData
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.Matchers._
import org.scalatest.{AsyncFeatureSpec, GivenWhenThen}

import scala.concurrent.Future

class AviatorsBotSigmetTest extends AsyncFeatureSpec with GivenWhenThen with AsyncMockFactory {
  info("As a pilot")
  info("I want to know which SIGMETs are active for a certain area")
  info("by just looking at a map of the SIGMETs")

  feature("SIGMET maps are sent upon the users request") {
    scenario("Pilot requests SIGMETs without selecting a region") {
      Given("AviatorsBotForTesting with mock for the sigmet service")
      val bot = new AviatorsBotForTesting()
      val testUrl = "/static/test.png"
      bot.sigmetService.get _ expects Regions.Europe returning
        Future.successful(PlotData(testUrl, Seq.empty, Map.empty))
      bot.regionsDAO.get _ expects * returning Future.successful(None)

      When("Pilot requests the sigmet map")
      bot.receiveMockMessage("/sigmet")

      Then("The photo is sent to the user")
      bot.photoSentFuture map { res =>
        res should endWith (testUrl)
      }
    }

    scenario("Pilot requests the SIGMET map for a different region") {
      Given("AviatorsBotForTesting with mock for the sigmet service")
      val bot = new AviatorsBotForTesting()
      val testUrl = "/static/test_oc.png"
      bot.sigmetService.get _ expects Regions.Oceania returning
        Future.successful(PlotData(testUrl, Seq.empty, Map.empty))
      bot.regionsDAO.get _ expects * returning Future.successful(None)

      When("Pilot requests the sigmet map")
      bot.receiveMockMessage("/sigmet oc")

      Then("The photo is sent to the user")
      bot.photoSentFuture map { res =>
        res should endWith (testUrl)
      }
    }
  }

}
