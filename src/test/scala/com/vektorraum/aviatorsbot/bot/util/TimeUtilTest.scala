package com.vektorraum.aviatorsbot.bot.util

import java.text.ParseException
import java.time.{Duration, LocalTime, ZonedDateTime}

import org.scalatest.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FunSuite, GivenWhenThen}

class TimeUtilTest extends FunSuite with TableDrivenPropertyChecks with GivenWhenThen {
  test("Time only matches various valid times") {
    Given("Various valid and invalid time strings")
    val properties = Table(
      ("time", "result"),
      ("1:20", true),
      ("120", true),
      ("120Z", true),
      ("0120Z", true),
      ("0120", true),
      ("01:20", true),
      ("12:15", true),
      ("23:59", true),
      ("0:20", true),
      ("020Z", true),
      ("24:00", false),
      ("20", false),
      ("000:00", false),
      ("01:010", false),
      ("01:20:30", false),
      ("1:20U", false)
    )

    Then("Only valid ones are identified as such")
    forAll (properties) { (time, result) =>
      TimeUtil.isTime(time) shouldEqual result
    }
  }

  test("Duration only matches valid durations") {
    Given("Various valid and invalid durations")
    val properties = Table(
      ("duration", "result"),
      ("01", true),
      ("90", true),
      ("1", true),
      ("99", true),
      ("120", false),
      ("12:14", false),
      ("-1", false)
    )

    Then("Only valid ones are identified as such")
    forAll (properties) { (duration, result) =>
      TimeUtil.isDuration(duration) shouldEqual result
    }
  }

  test("Conversion works correctly with time") {
    Given("Valid time inputs")
    val time1 = LocalTime.of(14, 15)

    val properties = Table(
      ("input", "parsedResult"),
      ("14:15", time1),
      ("1415", time1),
      ("14:15Z", time1)
    )

    Then("Parsing works correctly")
    forAll (properties) { (input, time) =>
      val result = TimeUtil.parseDurationOrTime(input)
      result.getHour shouldEqual time.getHour
      result.getMinute shouldEqual time.getMinute
      result.isAfter(ZonedDateTime.now()) shouldBe true
    }
  }

  test("Conversion works correctly with duration") {
    Given("Valid duration inputs")

    val properties = Table(
      ("input", "parsedResult"),
      ("1", 1),
      ("20", 20),
      ("99", 99)
    )

    Then("Parsing works correctly")
    forAll (properties) { (input, time) =>
      val result = TimeUtil.parseDurationOrTime(input)
      val minutes = Duration.between(ZonedDateTime.now(), result).toMinutes.toInt
      minutes should be < time * 60 + 2
      minutes should be > time * 60 - 2
    }
  }

  test("An invalid time format leads to a ParseException") {
    a[ParseException] should be thrownBy TimeUtil.parseDurationOrTime("12000")
  }

}
