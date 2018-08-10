package com.vektorraum.aviatorsbot.persistence.subscriptions.fixtures

import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription

trait SubscriptionFixtures {

  val chatId = 1234567
  val icao = "LOWW"
  val validUntil: Date = Date.from(ZonedDateTime.now(ZoneOffset.UTC).plusHours(1).toInstant)
  val validUntilExpired: Date = Date.from(ZonedDateTime.now(ZoneOffset.UTC).minusHours(1).toInstant)
  val subscription1 = Subscription(chatId, icao, validUntil)
  val subscription2 = Subscription(chatId, "KJFK", validUntil)
  val subscriptionExpired = Subscription(chatId, "KJAX", validUntilExpired)
}
