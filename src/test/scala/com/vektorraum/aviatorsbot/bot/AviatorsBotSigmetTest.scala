package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.persistence.WriteResultFixtures
import com.vektorraum.aviatorsbot.persistence.sigmets.model.SigmetInfo
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

  feature("Failures during the retrieval of the SIGMET Map are handled gracefully") {
    scenario("The backend server for the map fails") {
      Given("AviatorsBotForTesting with mock for the sigmet service which will fail")
      val bot = new AviatorsBotForTesting(ignoreMessages = 1)
      bot.regionsDAO.get _ expects * returning Future.successful(None)
      bot.sigmetService.get _ expects Regions.Europe returning
      Future.failed(new RuntimeException("EXPECTED"))

      When("Pilot requests the sigmet map")
      bot.receiveMockMessage("/sigmet")

      Then("An error message is sent to the pilot")
      bot.replyFuture map { reply =>
        reply should startWith("Could not retrieve the SIGMET map")
      }
    }

    scenario("The database fails while storing the SIGMET info with failed future") {
      Given("AviatorsBotForTesting with mock for the sigmet service")
      val bot = new AviatorsBotForTesting(ignoreMessages = 1)
      val testUrl = "/static/test.png"
      bot.sigmetService.get _ expects Regions.Europe returning
        Future.successful(PlotData(testUrl, Seq.empty, infoMap))
      bot.regionsDAO.get _ expects * returning Future.successful(None)
      bot.sigmetInfoDAO.store _ expects * returning Future.failed(new RuntimeException("EXPECTED"))

      When("Pilot requests the sigmet map")
      bot.receiveMockMessage("/sigmet")

      Then("The photo is sent and then an error message")
      bot.photoSentFuture flatMap { res =>
        res should endWith (testUrl)
        bot.replyFuture
      } map { reply =>
        reply should startWith ("Could not store the SIGMET information. Please run the /sigmet command again!")
      }
    }

    scenario("The database fails while storing the SIGMET info with failed write result") {
      Given("AviatorsBotForTesting with mock for the sigmet service")
      val bot = new AviatorsBotForTesting(ignoreMessages = 1)
      val testUrl = "/static/test.png"
      bot.sigmetService.get _ expects Regions.Europe returning
        Future.successful(PlotData(testUrl, Seq.empty, infoMap))
      bot.regionsDAO.get _ expects * returning Future.successful(None)
      bot.sigmetInfoDAO.store _ expects * returning Future.successful(WriteResultFixtures.WriteResultFailed)

      When("Pilot requests the sigmet map")
      bot.receiveMockMessage("/sigmet")

      Then("The photo is sent and then an error message is sent")
      bot.photoSentFuture flatMap { res =>
        res should endWith (testUrl)
        bot.replyFuture
      } map { reply =>
        reply should startWith ("Could not store the SIGMET information. Please run the /sigmet command again!")
      }
    }
  }

  feature("Requesting a specific SIGMET which was stored earlier") {
    scenario("Pilot requests a SIGMET info which is stored correctly") {
      Given("AviatorsBotForTesting with just the mock database")
      val bot = new AviatorsBotForTesting()
      bot.regionsDAO.get _ expects * returning Future.successful(None)
      val info = SigmetInfo(bot.testChatId, 3, "SIGMET TEST ONLY NOT FOR PRODUCTION USE")
      bot.sigmetInfoDAO.get _ expects(bot.testChatId, info.index) returning Future.successful(Some(info))

      When("Pilot requests the sigmet info")
      bot.receiveMockMessage("/sigmet 3")

      Then("The info is sent to the user")
      bot.replyFuture map { reply =>
        reply shouldEqual info.info
      }
    }

    scenario("Pilot requests a SIGMET info for an info which isn't stored") {
      Given("AviatorsBotForTesting with just the mock database")
      val bot = new AviatorsBotForTesting()
      bot.regionsDAO.get _ expects * returning Future.successful(None)
      val num = 10
      bot.sigmetInfoDAO.get _ expects(bot.testChatId, num) returning Future.successful(None)

      When("Pilot requests the sigmet info")
      bot.receiveMockMessage("/sigmet 10")

      Then("An error message is sent to the user")
      bot.replyFuture map { reply =>
        reply shouldEqual "No information stored for this number"
      }
    }

    scenario("Pilot requests a SIGMET info but the backend fails") {
      Given("AviatorsBotForTesting with just the mock database")
      val bot = new AviatorsBotForTesting()
      bot.regionsDAO.get _ expects * returning Future.successful(None)
      val num = 10
      bot.sigmetInfoDAO.get _ expects(bot.testChatId, num) returning Future.failed(new RuntimeException("EXPECTED"))

      When("Pilot requests the sigmet info")
      bot.receiveMockMessage("/sigmet 10")

      Then("An error message is sent to the user")
      bot.replyFuture map { reply =>
        reply shouldEqual "Could not load information"
      }
    }
  }

}
