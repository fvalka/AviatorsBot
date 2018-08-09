package com.vektorraum.aviatorsbot.bot

import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Date

import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import com.vektorraum.aviatorsbot.service.weather.fixtures.{AirfieldFixtures, METARResponseFixtures, TAFResponseFixtures}
import com.vektorraum.aviatorsbot.service.weather.mocks.AddsWeatherServiceForTest
import info.mukel.telegrambot4s.methods.ParseMode
import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers._
import org.scalatest.concurrent._
import org.scalatest.time.{Millis, Span}
import org.scalatest.{FeatureSpec, GivenWhenThen}
import reactivemongo.api.commands.DefaultWriteResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AviatorsBotSubscriptionsLsTest extends FeatureSpec with GivenWhenThen with MockFactory with Eventually {
  info("As a pilot I want to")
  info("be able to unsubscribe from weather updates")

}
