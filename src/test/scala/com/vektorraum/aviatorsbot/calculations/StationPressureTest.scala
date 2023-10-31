package com.vektorraum.aviatorsbot.calculations

import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks
import squants.{Length, MetricSystem}
import squants.motion.{Bars, Pressure}
import squants.space.{Feet, Meters}

class StationPressureTest extends AnyFunSuite with TableDrivenPropertyChecks with GivenWhenThen {
  implicit val tolerance: Pressure = Bars(MetricSystem.Milli * 0.02)

  test("Station pressure calculation for various valid data points") {
    Given("Various valid inputs")
    // Validation values were obtained from the NWS Density Altitude calculator:
    // https://www.weather.gov/epz/wxcalc_densityaltitude
    val properties = Table(
      ("elevation", "altimeterSetting", "stationPressure"),
      (Feet(1000), Bars(MetricSystem.Milli * 1013.0), Bars(MetricSystem.Milli * 976.91)),
      (Feet(10000), Bars(MetricSystem.Milli * 1013.0), Bars(MetricSystem.Milli * 696.49)),
      (Meters(500), Bars(MetricSystem.Milli * 990.0), Bars(MetricSystem.Milli * 932.67)),
      (Meters(0), Bars(MetricSystem.Milli * 1050.0), Bars(MetricSystem.Milli * 1050.0)),
      (Meters(-200), Bars(MetricSystem.Milli * 1013.0), Bars(MetricSystem.Milli * 1037.27))
    )

    Then("Precalculated values match the calculated ones")
    forAll(properties) { (elevation, altimeterSetting, stationPressure) =>
      StationPressure(elevation, altimeterSetting) ≈ stationPressure shouldEqual true
    }
  }
}
