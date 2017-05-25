package com.vektorraum.aviatorsbot.bot.xwind

import com.vektorraum.aviatorsbot.persistence.airfielddata.model.{Airfield, Runway}
import com.vektorraum.aviatorsbot.service.weather.fixtures.METARFixtures
import org.scalatest.{FunSuite, GivenWhenThen}
import org.scalatest.Matchers._

/**
  * Created by fvalka on 25.05.2017.
  */
class XWindCalculatorTest extends FunSuite with GivenWhenThen {
  object Fixtures {
    val NorthSouthRunway = Runway("18/36", List(180, 360))
    val AirfieldNorthSouthRwy = Airfield("XXXX", "XXXX", 0.0, List(NorthSouthRunway))
  }

  test("Normal wind") {
    Given("METAR containing normal wind, with no variation or gusts")
    val metar = METARFixtures.WindCases.normal

    When("crosswind is calculated")
    val result = XWindCalculator(metar, Fixtures.AirfieldNorthSouthRwy)

    Then("result matches precalculated values")
    result shouldEqual "<strong>18</strong> ⬅3KT ⬆5KT\n" +
      "<strong>36</strong> ➡3KT ⬇5KT"
  }

  test("Magnetic variation is considered correctly in runway names") {
    Given("METAR containing normal wind and Airfield with +10 deg magnetic variation")
    val metar = METARFixtures.WindCases.normal
    val airfield = Airfield("XXXX", "XXXX", 10.0, List(Fixtures.NorthSouthRunway))

    When("crosswind is calculated")
    val result = XWindCalculator(metar, airfield)

    Then("result matches precalculated values")
    result shouldEqual "<strong>17</strong> ⬅3KT ⬆5KT\n" +
      "<strong>35</strong> ➡3KT ⬇5KT"
  }

  test("Gusting wind and no head/tailwind component") {
    Given("METAR with gusting wind")
    val metar = METARFixtures.WindCases.gusting

    When("crosswind is calculated")
    val result = XWindCalculator(metar, Fixtures.AirfieldNorthSouthRwy)

    Then("result matches precalculated values and doesn't contain a head/tailwind")
    result shouldEqual "<strong>18</strong> ⬅37KT gusting ⬅47KT\n" +
      "<strong>36</strong> ➡37KT gusting ➡47KT"

  }

  test("Varying wind shows all variations") {
    Given("METAR with varying wind")
    val metar = METARFixtures.WindCases.varying090V170

    When("crosswind is calculated")
    val result = XWindCalculator(metar, Fixtures.AirfieldNorthSouthRwy)

    Then("result contains all variations of the wind")
    result shouldEqual "<strong>18</strong> ➡11KT ⬇9KT varying btw ➡14KT and ➡2KT ⬇14KT\n" +
      "<strong>36</strong> ⬅11KT ⬆9KT varying btw ⬅14KT and ⬅2KT ⬆14KT"
  }

  test("Fully variable wind (=VRBXX) gives error message") {
    Given("METAR with fully variable wind")
    val metar = METARFixtures.WindCases.fullyVariable

    When("crosswind is calculated")
    val result = XWindCalculator(metar, Fixtures.AirfieldNorthSouthRwy)

    Then("Error message is returned")
    result shouldEqual "⚠ Wind variable! No calculation possible."
  }

}