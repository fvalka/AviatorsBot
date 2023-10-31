package com.vektorraum.aviatorsbot.bot.weather

import com.vektorraum.aviatorsbot.service.weather.fixtures.METARFixtures
import org.scalatest.matchers.should.Matchers._
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite

/**
  * Created by fvalka on 20.05.2017.
  */
class FormatMetarTest extends AnyFunSuite with GivenWhenThen {

  test("FormatMetar should return the raw string with markup and emoji when neither diff nor sparklines are set") {
    Given("Valid METAR with VFR flight category")
    val metar = METARFixtures.ValidAndCompleteLOWW

    When("Formatting")
    val result = FormatMetar(List(metar))

    Then("Result contains METAR and MarkUp")
    result shouldEqual "<strong>LOWW</strong> ✅ 201920Z 32019KT 9999 FEW032 BKN036 16/09 Q1019 NOSIG"
  }

  test("FormatMetar returns an empty string for an empty METAR list") {
    FormatMetar(List()) shouldEqual ""
  }

  test("FormatMetar can deal with unknown flight categories") {
    Given("Valid METAR without flight category object")
    val metar = METARFixtures.ValidAndCompleteLOWW.copy(flight_category = None)

    When("Formatting")
    val result = FormatMetar(List(metar))

    Then("Result contains question mark and doesn't throw an error")
    result shouldEqual "<strong>LOWW</strong> ❔ 201920Z 32019KT 9999 FEW032 BKN036 16/09 Q1019 NOSIG"
  }

  test("FormatMetar can deal with MVFR") {
    Given("Valid METAR without flight category object")
    val metar = METARFixtures.ValidAndCompleteLOWW.copy(flight_category = Some("MVFR"))

    When("Formatting")
    val result = FormatMetar(List(metar))

    Then("Result contains the correct symbol")
    result should include ("⚠")
  }

  test("FormatMetar can deal with IFR") {
    Given("Valid METAR without flight category object")
    val metar = METARFixtures.ValidAndCompleteLOWW.copy(flight_category = Some("IFR"))

    When("Formatting")
    val result = FormatMetar(List(metar))

    Then("Result contains the correct symbol")
    result should include ("❗")
  }

  test("FormatMetar can deal with LIFR") {
    Given("Valid METAR without flight category object")
    val metar = METARFixtures.ValidAndCompleteLOWW.copy(flight_category = Some("LIFR"))

    When("Formatting")
    val result = FormatMetar(List(metar))

    Then("Result contains the correct symbol")
    result should include ("‼")
  }

}
