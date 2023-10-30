package com.vektorraum.aviatorsbot.bot.subscriptions

import com.bot4s.telegram.api.TelegramApiException
import com.vektorraum.aviatorsbot.bot.AviatorsBotForTesting
import com.vektorraum.aviatorsbot.persistence.WriteResultFixtures
import com.vektorraum.aviatorsbot.persistence.subscriptions.fixtures.SubscriptionFixtures
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.service.weather.fixtures.{METARResponseFixtures, METARResponseFixturesOlder, TAFResponseFixtures}
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers._
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Millis, Span}
import org.scalatest.{FeatureSpec, GivenWhenThen}

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

      mockNormalCalls(bot, sub1, sub2)

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

      mockNormalCalls(bot, sub1, sub2)

      When("Subscriptions have been handled once")
      bot.runSubscriptionHandler()
      bot.replySent shouldEqual "<strong>LOWW</strong> ✅ 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 " +
        "NOSIG\n<strong>TAF LOWW</strong> 231715Z 2318/2424 18004KT CAVOK TX22/2318Z TN12/2500Z FM240300 29015G25KT" +
        " 9999 BKN040 TEMPO 2403/2408 30018G30KT 6000 SHRA FEW030 FEW030CB BKN040 PROB30 2404/2407 4000 TSRA " +
        "FM241000 32015G25KT CAVOK TEMPO 2410/2416 33022G32KT BECMG 2416/2418 34012KT"
      bot.replySent = ""

      Then("Weather updates are not sent again")
      mockNormalCalls(bot, sub1, sub2)
      bot.runSubscriptionHandler()

      bot.replySent shouldEqual ""
    }

    scenario("Old weather updates are not sent when a newer weather update has already been sent") {
      Given("AviatorsBot with two subscriptions")
      val bot = new AviatorsBotForTesting()

      val sub1 = subscription1.copy()
      val sub2 = subscription2.copy()

      mockNormalCalls(bot, sub1, sub2)

      When("A newer update has already been sent")
      bot.runSubscriptionHandler()
      bot.replySent shouldEqual "<strong>LOWW</strong> ✅ 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 " +
        "NOSIG\n<strong>TAF LOWW</strong> 231715Z 2318/2424 18004KT CAVOK TX22/2318Z TN12/2500Z FM240300 29015G25KT" +
        " 9999 BKN040 TEMPO 2403/2408 30018G30KT 6000 SHRA FEW030 FEW030CB BKN040 PROB30 2404/2407 4000 TSRA " +
        "FM241000 32015G25KT CAVOK TEMPO 2410/2416 33022G32KT BECMG 2416/2418 34012KT"
      bot.replySent = ""

      Then("An older METAR update is received from the weather service but no message is sent")
      mockNormalCalls(bot, sub1, sub2)
      bot.weatherService.getMetars _ expects where {
        stations: Iterable[String] => stations.head == "LOWW"
      } returns Future.successful {
        METARResponseFixturesOlder.ValidLOWW7HoursOlder
      }
      bot.runSubscriptionHandler()
      bot.replySent shouldEqual ""
    }

    scenario("Only METARs are sent if the subscription is only for METARs") {
      Given("AviatorsBot with two subscriptions")
      val bot = new AviatorsBotForTesting()

      val sub1 = subscription1.copy(taf = false)
      val sub2 = subscription2.copy(taf = false)

      mockNormalCalls(bot, sub1, sub2)

      When("Handling subscriptions")
      bot.runSubscriptionHandler()

      Then("Only the METAR is sent to the pilot")
      bot.replySent shouldEqual "<strong>LOWW</strong> ✅ 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 " +
        "NOSIG"
    }

    scenario("Only TAFs are sent if the subscription is only for METARs") {
      Given("AviatorsBot with two subscriptions")
      val bot = new AviatorsBotForTesting()

      val sub1 = subscription1.copy(metar = false)
      val sub2 = subscription2.copy(metar = false)

      mockNormalCalls(bot, sub1, sub2)

      When("Handling subscriptions")
      bot.runSubscriptionHandler()

      Then("Only the TAF is sent to the pilot")
      bot.replySent shouldEqual "<strong>TAF LOWW</strong> 231715Z 2318/2424 18004KT CAVOK TX22/2318Z TN12/2500Z " +
        "FM240300 29015G25KT 9999 BKN040 TEMPO 2403/2408 30018G30KT 6000 SHRA FEW030 FEW030CB BKN040 PROB30 " +
        "2404/2407 4000 TSRA FM241000 32015G25KT CAVOK TEMPO 2410/2416 33022G32KT BECMG 2416/2418 34012KT"
    }

    scenario("Finding no subscriptions doesn't throw an exception and skips further processing") {
      Given("AviatorsBot with two subscriptions")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.findAllStations _ expects() returns
        Future.successful(Set.empty)

      When("Handling subscriptions")
      noException should be thrownBy bot.runSubscriptionHandler()
    }

  }

  feature("Database and Weather Service Errors are handled correclty") {
    scenario("Database throws an exception") {
      Given("AviatorsBot with two subscriptions")
      val bot = new AviatorsBotForTesting()

      bot.subscriptionDAO.findAllStations _ expects() returns
        Future.failed(new RuntimeException("TEST"))

      Then("A retriable exception is thrown")
      a[RetriableException] should be thrownBy bot.runSubscriptionHandler()
    }

    scenario("Weather service throws an exception") {
      Given("AviatorsBot with two subscriptions")
      val bot = new AviatorsBotForTesting()

      val sub1 = subscription1.copy()
      val sub2 = subscription2.copy()

      bot.subscriptionDAO.findAllStations _ expects() returns
        Future.successful(Set(sub1.icao, sub2.icao))

      bot.weatherService.getMetars _ expects where {
        stations: Iterable[String] => stations.head == "LOWW"
      } returns Future.failed {
        new RuntimeException("TEST")
      }

      Then("A retriable exception is thrown")
      a[RetriableException] should be thrownBy bot.runSubscriptionHandler()
    }

    scenario("Sending a message fails") {
      Given("AviatorsBot with two subscriptions")
      val bot = new AviatorsBotForTesting()

      val sub1 = subscription1.copy()
      val sub2 = subscription2.copy()

      bot.subscriptionDAO.findAllStations _ expects() returns
        Future.successful(Set(sub1.icao, sub2.icao))

      inAnyOrder {
        bot.weatherService.getMetars _ expects where {
          stations: Iterable[String] => stations.head == "LOWW"
        } returns Future.failed {
          new RuntimeException("TEST")
        }
        bot.weatherService.getTafs _ expects where {
          stations: Iterable[String] => stations.head == "LOWW"
        } returns Future.successful {
          TAFResponseFixtures.ValidLOWW
        }
      }

      bot.subscriptionDAO.findAllSubscriptionsForStation _ expects sub1.icao returns
        Future.successful(List(sub1))

      Then("A retriable exception is thrown and addOrExtend is never called")
      a[Exception] should be thrownBy bot.runSubscriptionHandler()
    }
  }

  feature("SendMessage exceptions are handled correctly") {
    scenario("The user has blocked the bot") {
      Given("AviatorsBot with two subscriptions")
      val ex = TelegramApiException("Forbidden: bot was blocked by the user", 403)
      val bot = new AviatorsBotForTesting(Some(ex))

      val sub1 = subscription1.copy()
      val sub2 = subscription2.copy()

      bot.subscriptionDAO.findAllStations _ expects() returns
        Future.successful(Set(sub1.icao, sub2.icao))

      normalWeatherServiceMock(bot)

      bot.subscriptionDAO.findAllSubscriptionsForStation _ expects sub1.icao returns
        Future.successful(List(sub1))

      Then("No message should be sent and the user has been unsubscribed from all stations, see mock")
      bot.subscriptionDAO.remove _ expects (sub1.chatId, "*") returns
        Future.successful(WriteResultFixtures.WriteResultOk)

      bot.runSubscriptionHandler()

      bot.replySent shouldEqual "<strong>LOWW</strong> ✅ 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 " +
        "NOSIG\n<strong>TAF LOWW</strong> 231715Z 2318/2424 18004KT CAVOK TX22/2318Z TN12/2500Z FM240300 29015G25KT" +
        " 9999 BKN040 TEMPO 2403/2408 30018G30KT 6000 SHRA FEW030 FEW030CB BKN040 PROB30 2404/2407 4000 TSRA " +
        "FM241000 32015G25KT CAVOK TEMPO 2410/2416 33022G32KT BECMG 2416/2418 34012KT"
    }

    scenario("A RuntimeException is thrown, which means that the subscription is not updated") {
      Given("AviatorsBot with two subscriptions")
      val ex = new RuntimeException()
      val bot = new AviatorsBotForTesting(Some(ex))

      val sub1 = subscription1.copy()
      val sub2 = subscription2.copy()

      bot.subscriptionDAO.findAllStations _ expects() returns
        Future.successful(Set(sub1.icao, sub2.icao))

      normalWeatherServiceMock(bot)

      bot.subscriptionDAO.findAllSubscriptionsForStation _ expects sub1.icao returns
        Future.successful(List(sub1))


      a[Exception] should be thrownBy bot.runSubscriptionHandler()
    }
  }

  private def normalWeatherServiceMock(bot: AviatorsBotForTesting) = {
    inAnyOrder {
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

  private def mockNormalCalls(bot: AviatorsBotForTesting, sub1: Subscription, sub2: Subscription) = {
    bot.subscriptionDAO.findAllStations _ expects() returns
      Future.successful(Set(sub1.icao, sub2.icao))

    normalWeatherServiceMock(bot)

    bot.subscriptionDAO.findAllSubscriptionsForStation _ expects sub1.icao returns
      Future.successful(List(sub1))

    bot.subscriptionDAO.addOrExtend _ expects * returns Future.successful(WriteResultFixtures.WriteResultOk)
  }

}
