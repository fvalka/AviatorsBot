package com.vektorraum.aviatorsbot.bot

import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent._
import org.scalatest.{FeatureSpec, GivenWhenThen}

class AviatorsBotSubscriptionsLsTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to")
  info("be able to unsubscribe from weather updates")

  feature("List subscriptions") {
    scenario("Users lists two stored subscriptions") {

    }
  }

}
