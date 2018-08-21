package com.vektorraum.aviatorsbot.calculations

import org.scalatest.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FunSuite, GivenWhenThen}
import squants.MetricSystem
import squants.motion.Bars
import squants.space.{Feet, Length, Meters}
import squants.thermal.{Celsius, Fahrenheit}

class DensityAltitudeTest extends FunSuite with TableDrivenPropertyChecks with GivenWhenThen {
  implicit val tolerance: Length = Feet(3)

  test("Density altitude calculations for various valid data points") {
    Given("Various valid inputs")
    // Validation values were obtained from the NWS Density Altitude calculator:
    // https://www.weather.gov/epz/wxcalc_densityaltitude
    val properties = Table(
      ("stationPressure", "temperature", "dewpoint", "densityAlt"),
      (Bars(MetricSystem.Milli * 1013.0), Celsius(30.0), Celsius(20.0), Feet(2044.3)),
      (Bars(1.013), Fahrenheit(90.0), Fahrenheit(60.0), Meters(676.1)),
      (Bars(0.8), Celsius(50.0), Fahrenheit(10.0), Feet(11559.6))
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
