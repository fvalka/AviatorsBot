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

  feature("Store subscriptions in the backend database using the DAO") {
    scenario("New subscription is added to the database") {
      Given("The DAO with an empty database")
      val dao: SubscriptionDAOProduction = new SubscriptionDAOProduction(db)
      val chatId = 1234567
      val icao = "LOWW"
      val validUntil = Date.from(ZonedDateTime.now(ZoneOffset.UTC).plusHours(1).toInstant)
      val subscription = Subscription(chatId, icao, validUntil)

      When("No subscription exists for the current user and a new one is added")

      Then("Test")

      cleanDb flatMap { _ =>
        dao.find(chatId, icao)
      } flatMap { result =>
        result shouldEqual None

        dao.addOrExtend(subscription)
      } flatMap { result =>
        result.ok shouldEqual true
      }
    }
  }

  private def cleanDb = {
    db.aviatorsDb flatMap { aviatorsDb =>
      aviatorsDb.drop()
    }
  }
}
