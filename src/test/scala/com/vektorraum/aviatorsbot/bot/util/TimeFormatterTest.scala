package com.vektorraum.aviatorsbot.bot.util

import java.time.{ZoneOffset, ZonedDateTime}

import org.scalatest.Matchers._
import org.scalatest.{FunSuite, GivenWhenThen}

/**
  * Created by fvalka on 30.05.2017.
  */
class TimeFormatterTest extends FunSuite with GivenWhenThen {

  test("shortUTCDateTimeFormat returns only the hour and minute if the input is a date on this day") {
    Given("A UTC date which is today")
    val dateTime = ZonedDateTime.now(ZoneOffset.UTC).withHour(23).withMinute(59)

    When("Formatting the date")
    val result = TimeFormatter.shortUTCDateTimeFormat(dateTime)

    Then("Output only contains the hour and minute")
    result shouldEqual "2359Z"
  }

  test("shortUTCDateTimeFormat returns a fully formatted string for a date which is not today") {
    Given("A date which is in the past and before today")
    val dateTime = ZonedDateTime.of(2000,2,1,13,48,34,54, ZoneOffset.UTC)

    When("Formatting the date")
    val result = TimeFormatter.shortUTCDateTimeFormat(dateTime)

    Then("Output is a full date")
    result shouldEqual "2000-2-1 1348Z"

  }

}
