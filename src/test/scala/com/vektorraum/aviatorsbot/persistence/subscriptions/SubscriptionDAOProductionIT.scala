package com.vektorraum.aviatorsbot.persistence.subscriptions

import java.io.File
import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import com.softwaremill.macwire._
import com.typesafe.config.{Config, ConfigFactory}
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.persistence.Db
import org.scalatest.Matchers._
import org.scalatest.{AsyncFeatureSpec, GivenWhenThen, _}

import scala.language.postfixOps

class SubscriptionDAOProductionIT extends AsyncFeatureSpec with GivenWhenThen {
  info("As a pilot I want to be able to subscribe to weather stations")
  info("and make sure that those subscriptions will lead to reliable")
  info("weather updates and don't want to have to take care of restarts or technical details")


  protected val config: Config = ConfigFactory.parseFile(new File("conf/aviatorsbot-test.conf"))
  val db: Db = wire[Db]
  val dao: SubscriptionDAOProduction = new SubscriptionDAOProduction(db)

  val chatId = 1234567
  val icao = "LOWW"
  val validUntil = Date.from(ZonedDateTime.now(ZoneOffset.UTC).plusHours(1).toInstant)
  val validUntilExpired = Date.from(ZonedDateTime.now(ZoneOffset.UTC).minusHours(1).toInstant)
  val subscription1 = Subscription(chatId, icao, validUntil)
  val subscription2 = Subscription(1233333, "KJFK", validUntil)
  val subscriptionExpired = Subscription(2222222, "KJAX", validUntilExpired)

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

    scenario("All stations ICAO codes of all stations with active subscriptions can be listed") {
      Given("A database with stored subscriptions")

      cleanDb flatMap { _ =>
        dao.addOrExtend(subscription1)
        dao.addOrExtend(subscription2)
        dao.addOrExtend(subscriptionExpired)
      } flatMap { _ =>
        dao.findAllStations()
      } flatMap { stations =>
        stations should contain (subscription1.icao)
        stations should contain (subscription2.icao)
        stations should not contain subscriptionExpired.icao
      }
    }
  }

  private def cleanDb = {
    db.aviatorsDb flatMap { aviatorsDb =>
      aviatorsDb.drop()
    }
  }
}
