package com.vektorraum.aviatorsbot.bot.calculators

import com.vektorraum.aviatorsbot.persistence.airfielddata.model.Airfield
import com.vektorraum.aviatorsbot.service.weather.fixtures.{AirfieldFixtures, METARFixtures}
import org.scalatest.matchers.should.Matchers._
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite

/**
  * Created by fvalka on 25.05.2017.
  */
class XWindCalculatorTest extends AnyFunSuite with GivenWhenThen {

  test("Normal wind") {
    Given("METAR containing normal wind, with no variation or gusts")
    val metar = METARFixtures.WindCases.normal

    When("crosswind is calculated")
    val result = XWindCalculator(metar, AirfieldFixtures.AirfieldNorthSouthRwy)

    Then("result matches precalculated values")
    result shouldEqual "<strong>18</strong> ⬅  3KT ⬆  5KT\n" +
      "<strong>36</strong> ➡  3KT ⬇  5KT"
  }

  test("Magnetic variation is considered correctly in runway names") {
    Given("METAR containing normal wind and Airfield with +10 deg magnetic variation")
    val metar = METARFixtures.WindCases.normal
    val airfield = Airfield("XXXX", "XXXX", 10.0, List(AirfieldFixtures.RunwayFixtures.NorthSouthRunway))

    When("crosswind is calculated")
    val result = XWindCalculator(metar, airfield)

    Then("result matches name in the airfield information")
    result shouldEqual "<strong>18</strong> ⬅  3KT ⬆  5KT\n" +
      "<strong>36</strong> ➡  3KT ⬇  5KT"
  }

  test("Given a difference which is too large between the runway number and the direction the direction is used") {
    Given("METAR containing normal wind and Airfield with +10 deg magnetic variation")
    val metar = METARFixtures.WindCases.normal
    val airfield = Airfield("XXXX", "XXXX", 11.0, List(AirfieldFixtures.RunwayFixtures.NorthSouthRunway))

    When("crosswind is calculated")
    val result = XWindCalculator(metar, airfield)

    Then("result matches name in the airfield information")
    result shouldEqual "<strong>17</strong> ⬅  3KT ⬆  5KT\n" +
      "<strong>35</strong> ➡  3KT ⬇  5KT"
  }

  test("Gusting wind and no head/tailwind component") {
    Given("METAR with gusting wind")
    val metar = METARFixtures.WindCases.gusting

    When("crosswind is calculated")
    val result = XWindCalculator(metar, AirfieldFixtures.AirfieldNorthSouthRwy)

    Then("result matches precalculated values and doesn't contain a head/tailwind")
    result shouldEqual "<strong>18</strong> ⬅37KT \n      gusting ⬅47KT\n" +
      "<strong>36</strong> ➡37KT \n      gusting ➡47KT"

  }

  test("Varying wind shows all variations") {
    Given("METAR with varying wind")
    val metar = METARFixtures.WindCases.varying090V170

    When("crosswind is calculated")
    val result = XWindCalculator(metar, AirfieldFixtures.AirfieldNorthSouthRwy)

    Then("result contains all variations of the wind")
    result shouldEqual "<strong>18</strong> ➡11KT ⬇  9KT \n      var ➡14KT &amp; ➡  2KT ⬇14KT\n" +
      "<strong>36</strong> ⬅11KT ⬆  9KT \n      var ⬅14KT &amp; ⬅  2KT ⬆14KT"
  }

  test("Fully variable wind (=VRBXX) gives error message") {
    Given("METAR with fully variable wind")
    val metar = METARFixtures.WindCases.fullyVariable

    When("crosswind is calculated")
    val result = XWindCalculator(metar, AirfieldFixtures.AirfieldNorthSouthRwy)

    Then("Error message is returned")
    result shouldEqual "⚠ Wind variable! No calculation possible."
  }

  test("One ended runway gives the correct output") {
    Given("METAR containing normal wind, with no variation or gusts")
    val metar = METARFixtures.WindCases.normal

    When("crosswind is calculated")
    val result = XWindCalculator(metar, AirfieldFixtures.AirfieldOneEnded)

    Then("result matches pre calculated values")
    result shouldEqual "<strong>36</strong> ➡  3KT ⬇  5KT"
  }

  test("Wrong runway heading by 10deg gives correct runway name") {
    Given("METAR containing normal wind, with no variation or gusts")
    val metar = METARFixtures.WindCases.normal

    When("crosswind is calculated")
    val result = XWindCalculator(metar, AirfieldFixtures.AirfieldNorthSouthRwy10degOff)

    Then("result matches precalculated values")
    result shouldEqual "<strong>18</strong> ⬅  2KT ⬆  6KT\n" +
      "<strong>36</strong> ➡  2KT ⬇  6KT"
  }

  test("Wrong runway heading by 15deg gives calculated runway name") {
    Given("METAR containing normal wind, with no variation or gusts")
    val metar = METARFixtures.WindCases.normal

    When("crosswind is calculated")
    val result = XWindCalculator(metar, AirfieldFixtures.AirfieldNorthSouthRwy15degOff)

    Then("result matches precalculated values")
    result shouldEqual "<strong>02</strong> ➡  4KT ⬇  4KT\n" +
      "<strong>20</strong> ⬅  4KT ⬆  4KT"
  }

}
