package com.vektorraum.aviatorsbot.bot.calculators

import com.vektorraum.aviatorsbot.calculations.StationPressure
import com.vektorraum.aviatorsbot.service.weather.fixtures.{AirfieldFixtures, METARFixtures}
import org.scalatest.{FunSuite, GivenWhenThen}
import org.scalatest.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks

class DensityAltitudeCalculatorTest extends FunSuite with GivenWhenThen with TableDrivenPropertyChecks {
  test("Density Altitude for valid METAR") {
    Given("METAR containing normal wind, with no variation or gusts")
    val metar = METARFixtures.ValidAndCompleteLOWW

    When("Density altitude is calculated")
    val result = DensityAltitudeCalculator(metar)

    Then("result matches precalculated values")
    result shouldEqual "METAR observation time: 2017-5-20 1920Z\n" +
      "Elevation: 623 ft\n" +
      "Density altitude: <strong>865 ft</strong>"
  }

  test("Missing values result in error message") {
    Given("METARs with missing values")
    val properties = Table(
      "invalid metar",
      METARFixtures.ValidAndCompleteLOWW.copy(temp_c = None),
      METARFixtures.ValidAndCompleteLOWW.copy(dewpoint_c = None),
      METARFixtures.ValidAndCompleteLOWW.copy(altim_in_hg = None),
      METARFixtures.ValidAndCompleteLOWW.copy(elevation_m = None)
    )

    Then("Error message is returned")
    forAll(properties) { metar =>
      DensityAltitudeCalculator(metar) shouldEqual "Missing values in METAR, calculation can not be performed."
    }
  }

  test("Missing observation time returns valid message with information about the problem") {
    Given("METAR containing normal wind, with no variation or gusts")
    val metar = METARFixtures.ValidAndCompleteLOWW.copy(observation_time = None)

    When("Density altitude is calculated")
    val result = DensityAltitudeCalculator(metar)

    Then("result matches precalculated values")
    result shouldEqual "METAR observation time: Unknown\n" +
      "Elevation: 623 ft\n" +
      "Density altitude: <strong>865 ft</strong>"
  }

}
