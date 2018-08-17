package com.vektorraum.aviatorsbot.bot.subscriptions

import com.vektorraum.aviatorsbot.bot.AviatorsBotForTesting
import com.vektorraum.aviatorsbot.persistence.subscriptions.fixtures.SubscriptionFixtures
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.service.weather.fixtures.{METARResponseFixtures, TAFResponseFixtures}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{AsyncFeatureSpec, FeatureSpec, GivenWhenThen}
import org.scalatest.concurrent.Eventually
import org.scalatest.Matchers._
import org.scalatest.time.{Millis, Span}

import scala.concurrent.Future

class SubscriptionHandlerTest extends FeatureSpec
  with GivenWhenThen with MockFactory with Eventually with SubscriptionFixtures  {
  info("As a pilot I want to be able to receive continuous weather updates")

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(500, Millis)), interval = scaled(Span(30, Millis)))

  feature("SubscriptionHandler handles valid subscriptions") {
    scenario("As a pilot I want to receive weather updates when first subscribing") {
      Given("AviatorsBot with two subscriptions")
      val bot = new AviatorsBotForTesting()

      val sub1 = subscription1.copy()
      val sub2 = subscription2.copy()

      mockTwoStations(bot, sub1, sub2)
      mockValidWeather(bot)

      When("Handling subscriptions")
      bot.runSubscriptionHandler()

      Then("A weather update has been sent to the pilot")
      bot.replySent shouldEqual "<strong>LOWW</strong> ✅ 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 " +
        "NOSIG\n<strong>TAF LOWW</strong> 231715Z 2318/2424 18004KT CAVOK TX22/2318Z TN12/2500Z FM240300 29015G25KT" +
        " 9999 BKN040 TEMPO 2403/2408 30018G30KT 6000 SHRA FEW030 FEW030CB BKN040 PROB30 2404/2407 4000 TSRA " +
        "FM241000 32015G25KT CAVOK TEMPO 2410/2416 33022G32KT BECMG 2416/2418 34012KT"
    }

    scenario("Weather updates are not sent twice") {
      Given("AviatorsBot with two subscriptions")
      val bot = new AviatorsBotForTesting()

      val sub1 = subscription1.copy()
      val sub2 = subscription2.copy()

      mockTwoStations(bot, sub1, sub2)
      mockValidWeather(bot)

      When("Subscriptions have been handled once")
      bot.runSubscriptionHandler()
      bot.replySent shouldEqual "<strong>LOWW</strong> ✅ 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 " +
        "NOSIG\n<strong>TAF LOWW</strong> 231715Z 2318/2424 18004KT CAVOK TX22/2318Z TN12/2500Z FM240300 29015G25KT" +
        " 9999 BKN040 TEMPO 2403/2408 30018G30KT 6000 SHRA FEW030 FEW030CB BKN040 PROB30 2404/2407 4000 TSRA " +
        "FM241000 32015G25KT CAVOK TEMPO 2410/2416 33022G32KT BECMG 2416/2418 34012KT"
      bot.replySent = ""

      Then("Weather updates are not sent again")
      mockTwoStations(bot, sub1, sub2)
      mockValidWeather(bot)
      bot.runSubscriptionHandler()

      bot.replySent shouldEqual ""
    }

  }

  private def mockTwoStations(bot: AviatorsBotForTesting, sub1: Subscription, sub2: Subscription) = {
    bot.subscriptionDAO.findAllStations _ expects() returns
      Future.successful(Set(sub1.icao, sub2.icao))
    bot.subscriptionDAO.findAllSubscriptionsForStation _ expects sub1.icao returns
      Future.successful(List(sub1))
    bot.subscriptionDAO.findAllSubscriptionsForStation _ expects sub2.icao returns
      Future.successful(List(sub2))
  }

  private def mockValidWeather(bot: AviatorsBotForTesting) = {
    bot.weatherService.getMetars _ expects where {
      stations: Iterable[String] => stations.head == "LOWW"
    } returns Future.successful {
      METARResponseFixtures.ValidLOWW7Hours
    }
    bot.weatherService.getTafs _ expects where {
      stations: Iterable[String] => stations.head == "LOWW"
    } returns Future.successful {
      TAFResponseFixtures.ValidLOWW
    }
  }
}
