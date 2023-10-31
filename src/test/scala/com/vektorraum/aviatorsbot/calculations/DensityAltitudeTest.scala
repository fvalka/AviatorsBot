package com.vektorraum.aviatorsbot.calculations

import org.scalatest.matchers.should.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite
import squants.MetricSystem
import squants.motion.Bars
import squants.space.{Feet, Length, Meters}
import squants.thermal.{Celsius, Fahrenheit}

class DensityAltitudeTest extends AnyFunSuite with TableDrivenPropertyChecks with GivenWhenThen {
  implicit val tolerance: Length = Feet(2)

  test("Density altitude calculations for various valid data points") {
    Given("Various valid inputs")
    // Validation values were obtained from the NWS Density Altitude calculator:
    // https://www.weather.gov/epz/wxcalc_densityaltitude
    val properties = Table(
      ("stationPressure", "temperature", "dewpoint", "densityAlt"),
      (Bars(MetricSystem.Milli * 1013.0), Celsius(30.0), Celsius(20.0), Feet(2044.3)),
      (Bars(1.013), Fahrenheit(90.0), Fahrenheit(60.0), Meters(676.1)),
      (Bars(0.8), Celsius(50.0), Fahrenheit(10.0), Feet(11559.6)),
      (Bars(MetricSystem.Milli * 1090), Celsius(10.0), Celsius(5.0), Feet(-3001.2)),
      (Bars(MetricSystem.Milli * 400), Celsius(-40.0), Celsius(-50.0), Feet(22577.2)),
      (Bars(MetricSystem.Milli * 400), Celsius(-40.0), Celsius(-40.0), Feet(22580.6)),
      (Bars(MetricSystem.Milli * 150), Celsius(-40.0), Celsius(-50.0), Feet(47856.6))
    )

    Then("Only valid ones are identified as such")
    forAll (properties) { (stationPressure, temperature, dewpoint, densityAlt) =>
      DensityAltitude(stationPressure, temperature, dewpoint) ≈ densityAlt shouldEqual true
    }
  }

  test("Invalid dewpoint leads to an exception") {
    an[IllegalArgumentException] should be thrownBy
      DensityAltitude(Bars(MetricSystem.Milli * 1013.0), Celsius(30.0), Celsius(31.0))
  }

  test("Negative pressures are not allowed") {
    an[IllegalArgumentException] should be thrownBy
      DensityAltitude(Bars(MetricSystem.Milli * -1013.0), Celsius(30.0), Celsius(20.0))
  }

}
