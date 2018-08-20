package com.vektorraum.aviatorsbot.calculations

import com.codahale.metrics.MetricRegistry.MetricSupplier
import com.vektorraum.aviatorsbot.bot.util.TimeUtil
import org.scalatest.{FunSuite, GivenWhenThen}
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.Matchers._
import squants.{MetricSystem, Temperature}
import squants.motion.Bars
import squants.space.{Feet, Length}
import squants.thermal.Celsius

class DensityAltitudeTest extends FunSuite with TableDrivenPropertyChecks with GivenWhenThen {
  implicit val tolerance: Length = Feet(3)

  test("Density altitude calculations for various valid data points") {
    Given("Various valid inputs")
    // Validation values were obtained from the NWS Density Altitude calculator:
    // https://www.weather.gov/epz/wxcalc_densityaltitude
    val properties = Table(
      ("stationPressure", "temperature", "dewpoint", "densityAlt"),
      (Bars(MetricSystem.Milli * 1013.0), Celsius(30.0), Celsius(20.0), Feet(2044.3))
    )

    Then("Only valid ones are identified as such")
    forAll (properties) { (stationPressure, temperature, dewpoint, densityAlt) =>
      DensityAltitude(stationPressure, temperature, dewpoint) ≈ densityAlt shouldEqual true
    }
  }

}
