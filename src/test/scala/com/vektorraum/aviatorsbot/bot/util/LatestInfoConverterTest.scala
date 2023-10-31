package com.vektorraum.aviatorsbot.bot.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import com.vektorraum.aviatorsbot.service.weather.fixtures.{METARFixtures, TAFFixtures}
import org.scalatest.matchers.should.Matchers._
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite

class LatestInfoConverterTest extends AnyFunSuite with GivenWhenThen {

  test("Valid metar conversion") {
    Given("A valid METAR")
    val input = METARFixtures.ValidAndCompleteLOWW

    When("Converting to a LatestInfo twice")
    val conv1 = LatestInfoConverter.fromMetar(input)
    val conv2 = LatestInfoConverter.fromMetar(input)

    Then("Both conversions are equal and the date is correct")
    conv1 shouldEqual conv2
    conv1.issuedAt shouldEqual Date.from(
      ZonedDateTime.parse(
        input.observation_time.get, DateTimeFormatter.ISO_ZONED_DATE_TIME
      ).toInstant
    )
    conv1.hash should not be 0
  }

  test("Valid taf conversion") {
    Given("A valid TAF")
    val input = TAFFixtures.ValidAndCompleteLOWW

    When("Converting to LatestInfo twice")
    val conv1 = LatestInfoConverter.fromTaf(input)
    val conv2 = LatestInfoConverter.fromTaf(input)

    Then("Both conversions are equal and the date is correct")
    conv1 shouldEqual conv2
    conv1.issuedAt shouldEqual Date.from(
      ZonedDateTime.parse(
        input.issue_time.get, DateTimeFormatter.ISO_ZONED_DATE_TIME
      ).toInstant
    )
    conv1.hash should not be 0
  }

}
