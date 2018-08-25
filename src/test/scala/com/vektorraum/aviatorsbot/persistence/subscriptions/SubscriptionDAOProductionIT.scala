package com.vektorraum.aviatorsbot.persistence.subscriptions

import java.io.File
import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import com.softwaremill.macwire._
import com.typesafe.config.{Config, ConfigFactory}
import com.vektorraum.aviatorsbot.persistence.Db
import com.vektorraum.aviatorsbot.persistence.subscriptions.fixtures.SubscriptionFixtures
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import org.scalatest.Matchers._
import org.scalatest.{AsyncFeatureSpec, GivenWhenThen}

import scala.language.postfixOps

class SubscriptionDAOProductionIT extends AsyncFeatureSpec with GivenWhenThen with SubscriptionFixtures {
  info("As a pilot I want to be able to subscribe to weather stations")
  info("and make sure that those subscriptions will lead to reliable")
  info("weather updates and don't want to have to take care of restarts or technical details")


  protected val config: Config = ConfigFactory.parseFile(new File("conf/aviatorsbot-test.conf"))
  val db: Db = wire[Db]
  val dao: SubscriptionDAOProduction = new SubscriptionDAOProduction(db)

  feature("Store subscriptions in the backend database using the DAO") {
    scenario("New subscription is added to the database") {
      Given("The DAO with an empty database")

      When("No subscription exists for the current user and a new one is added")

      cleanDb flatMap { _ =>
        dao.find(chatId, icao)
      } flatMap { result =>
        result shouldEqual None

        dao.addOrExtend(subscription1)
      } flatMap { result =>
        result.ok shouldEqual true

        dao.find(chatId, icao)
      } flatMap { result =>
        Then("The dao should return the correct subscription when find is used")
        result should not be empty
        result.get.chatId shouldEqual chatId
        result.get.icao shouldEqual icao
        result.get.validUntil shouldEqual validUntil
      }
    }
  }

  feature("Find all stations") {
    scenario("All stations ICAO codes of all stations with active subscriptions can be listed") {
      Given("A database with stored subscriptions")

      cleanDb flatMap { _ =>
        dao.addOrExtend(subscription1)
        dao.addOrExtend(subscription2)
        dao.addOrExtend(subscriptionExpired)
      } flatMap { _ =>
        When("Finding all stations/obtaining a list of all subscribed stations")
        dao.findAllStations()
      } flatMap { stations =>
        Then("All stations with non expired subscriptions are included")
        stations should contain(subscription1.icao)
        stations should contain(subscription2.icao)
        stations should not contain subscriptionExpired.icao
      }
    }
  }

  feature("Add or extend a subscription") {
    scenario("AddOrExtend automatically extends the validity if a station is added again") {
      Given("A subscription which is valid for one more hour and a copy of this subscription" +
        "which is valid for 2 hours")
      val validUntilLonger = Date.from(ZonedDateTime.now(ZoneOffset.UTC).plusHours(2).toInstant)
      val subscriptionShortValidity = subscription1
      val subscriptionValidLonger = subscriptionShortValidity.copy(validUntil = validUntilLonger)

      cleanDb flatMap { _ =>
        dao.addOrExtend(subscriptionShortValidity)
      } flatMap { writeResult =>
        When("Adding a subscription for the same chatId/Icao station with different validity")
        writeResult.ok should be(true)
        dao.addOrExtend(subscriptionValidLonger)
      } flatMap { writeResult =>
        Then("Find will return the longer validity")
        writeResult.ok should be(true)
        dao.find(subscriptionShortValidity.chatId, subscriptionShortValidity.icao)
      } flatMap { subscription =>
        subscription should not be empty
        subscription.get.validUntil shouldEqual validUntilLonger
      }

    }
  }

  feature("Find all subscriptions for a specific user/chatId") {
    scenario("FindAllByChatId returns all stations with non expired subscriptions") {
      Given("Two subscriptions for the same chatId")

      cleanDb flatMap { _ =>
        dao.addOrExtend(subscription1)
        dao.addOrExtend(subscription2)
        dao.addOrExtend(subscriptionExpired)
      } flatMap { _ =>
        When("Finding all subscriptions for a specific chatId")
        dao.findAllByChatId(subscription1.chatId)
      } flatMap { subs =>
        Then("The non expired subscriptions are contained in the list and the expired one is not part" +
          "of the result")
        subs should contain(subscription1)
        subs should contain(subscription2)
        subs should not contain subscriptionExpired
      }
    }
  }

  feature("Removing subscriptions") {
    scenario("Remove of a subscription which doesn't exist returns the correct writeResult") {
      Given("No subscriptions in the database")

      cleanDb flatMap { _ =>
        When("Removing a subscription which doesnt exist")
        dao.remove(subscription1.chatId, subscription1.icao)
      } flatMap { result =>
        result.ok shouldBe true
      }
    }

    scenario("Wildcards work when removing stations from the database") {
      Given("A subscription stored in the database")

      cleanDb flatMap { _ =>
        dao.addOrExtend(subscription1)
        dao.addOrExtend(subscription2)
      } flatMap { _ =>
        When("Removing  all stations using wildcard")
        dao.remove(subscription1.chatId, "*")
      } flatMap { writeResult =>
        writeResult.ok shouldBe true
        dao.findAllByChatId(subscription1.chatId)
      } flatMap { result =>
        Then("The dao should no longer find this subscription")
        result shouldBe empty
      }
    }

    scenario("Limited wildcard only removes the correct station") {
      Given("A subscription stored in the database")

      cleanDb flatMap { _ =>
        dao.addOrExtend(subscription1)
        dao.addOrExtend(subscription2)
      } flatMap { _ =>
        When("Removing only stations with the first letter L")
        dao.remove(subscription1.chatId, "L*")
      } flatMap { writeResult =>
        writeResult.ok shouldBe true
        dao.findAllByChatId(subscription1.chatId)
      } flatMap { result =>
        Then("The dao should no longer find this subscription")
        result should contain (subscription2)
      }
    }

    scenario("Adding a subscription and then removing it actually removes it from the database") {
      Given("Two subscription stored in the database")

      cleanDb flatMap { _ =>
        dao.addOrExtend(subscription1)
      } flatMap { _ =>
        When("Removing the subscription from the database")
        dao.remove(subscription1.chatId, subscription1.icao)
      } flatMap { writeResult =>
        writeResult.ok shouldBe true
        dao.find(subscription1.chatId, subscription1.icao)
      } flatMap { result =>
        Then("The dao should no longer find this subscription")
        result shouldEqual None
      }
    }
  }

  feature("Counting subscriptions") {
    scenario("Count returns correct number of subscriptions") {
      Given("A database with stored subscriptions")

      cleanDb flatMap { _ =>
        dao.addOrExtend(subscription1)
        dao.addOrExtend(subscription2)
        dao.addOrExtend(subscriptionExpired)
      } flatMap { _ =>
        When("Counting all subscriptions in the database")
        dao.count()
      } flatMap { num =>
        Then("Count should only include active subscriptions")
        num shouldEqual 2
      }
    }
  }

  feature("Listing all subscriptions for a specific station") {
    scenario("When subscriptions are stored in the database all " +
      "subscriptions are returned when querying for this station") {
      Given("Subscriptions stored in the database")

      cleanDb flatMap { _ =>
        dao.addOrExtend(subscription1)
        dao.addOrExtend(subscription2)
        dao.addOrExtend(subscription3)
      } flatMap { _ =>
        When("Finding all stations by ICAO code")
        dao.findAllSubscriptionsForStation(subscription1.icao)
      } flatMap { result =>
        Then("All subscriptions with this icao id should be found")
        result should contain (subscription1)
        result should contain (subscription3)
        result should not contain subscription2
      }
    }

    scenario("No subscriptions for station") {
      Given("Empty database")

      cleanDb flatMap { _ =>
        When("Finding all stations by ICAO code")
        dao.findAllSubscriptionsForStation(subscription1.icao)
      } flatMap { result =>
        Then("The list should be empty")
        result.isEmpty shouldEqual true
      }
    }
  }

  /**
    * Used instead of before/after methods because it is also async which
    * is not supported by ScalaTest in the current version
    *
    * @return Future, which when completed means that the db is ready for
    *         testing
    */
  private def cleanDb = {
    db.aviatorsDb flatMap { aviatorsDb =>
      aviatorsDb.drop()
    }
  }
}
