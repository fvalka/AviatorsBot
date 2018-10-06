package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.persistence.WriteResultFixtures
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

  val infoMap: Map[Int, String] = Map(1 -> "First info test", 2 -> "Second Info Test!")

  feature("SIGMET maps are sent upon the users request") {
    scenario("Pilot requests SIGMETs without selecting a region") {
      Given("AviatorsBotForTesting with mock for the sigmet service")
      val bot = new AviatorsBotForTesting()
      val testUrl = "/static/test.png"
      bot.sigmetService.get _ expects Regions.Europe returning
        Future.successful(PlotData(testUrl, Seq.empty, infoMap))
      bot.regionsDAO.get _ expects * returning Future.successful(None)
      bot.sigmetInfoDAO.store _ expects * returning Future.successful(WriteResultFixtures.WriteResultOk)

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
        Future.successful(PlotData(testUrl, Seq.empty, infoMap))
      bot.regionsDAO.get _ expects * returning Future.successful(None)
      bot.sigmetInfoDAO.store _ expects * returning Future.successful(WriteResultFixtures.WriteResultOk)

      When("Pilot requests the sigmet map")
      bot.receiveMockMessage("/sigmet oc")

      Then("The photo is sent to the user")
      bot.photoSentFuture map { res =>
        res should endWith (testUrl)
      }
    }

    scenario("When requesting the SIGMET an info message is sent to the user") {
      Given("AviatorsBotForTesting with mock for the sigmet service")
      val bot = new AviatorsBotForTesting()
      val testUrl = "/static/test_oc.png"
      bot.sigmetService.get _ expects Regions.Oceania returning
        Future.successful(PlotData(testUrl, Seq.empty, infoMap))
      bot.regionsDAO.get _ expects * returning Future.successful(None)
      bot.sigmetInfoDAO.store _ expects * returning Future.successful(WriteResultFixtures.WriteResultOk)

      When("Pilot requests the sigmet map")
      bot.receiveMockMessage("/sigmet oc")

      Then("The photo is sent to the user")
      bot.replyFuture map { reply =>
        reply should startWith("Drawing weather map. This might take up to")
      }
    }
  }

}
