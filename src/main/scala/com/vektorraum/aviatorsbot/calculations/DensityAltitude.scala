package com.vektorraum.aviatorsbot.calculations

import squants.MetricSystem
import squants.motion.{Bars, Pascals, Pressure}
import squants.space.{Feet, Length}
import squants.thermal.{Celsius, Kelvin, Rankine, Temperature}

object DensityAltitude {

  /**
    * Density altitude calculator which considers all relevant atmospheric properties.
    *
    * Based upon the equations published by NOAA on weather.gov:
    * https://www.weather.gov/media/epz/wxcalc/densityAltitude.pdf
    *
    * @param stationPressure Actual station pressure, not altimeter setting/QNH
    * @param temperature Temperature at pressureAlt
    * @param dewpoint Dew point at pressureAlt
    * @return Density altitude
    */
  def apply(stationPressure: Pressure, temperature: Temperature, dewpoint: Temperature): Length = {
    require(temperature >= dewpoint)
    require(stationPressure > Pascals(0))

    val e = vaporPressure(dewpoint)
    val T_v = virtualTemperature(temperature, e, stationPressure)
    val base = 511.6 * (stationPressure.to(Bars)/T_v.to(Rankine))

    Feet(145366) * (1 - Math.pow(base, 0.235))
  }

  /**
    * Calculates the vapor pressure for a given dew point
    *
    * @param dewpoint Dew point for vapor pressure calculation
    * @return Vapor pressure for this dew point
    */
  protected def vaporPressure(dewpoint: Temperature): Pressure =
    Bars(6.11  * MetricSystem.Milli) *
      math.pow(10, (7.5 * dewpoint.in(Celsius))/(Celsius(237.7) + dewpoint.in(Celsius)))

  /**
    * Calculates the virtual temperature required for the density altitude calculation
    *
    * @param temperature Temperature at pressureAlt
    * @param vaporPressure Vapor pressure
    * @param stationPressure Station Pressure, not altimeter setting/QNH
    * @return
    */
  protected def virtualTemperature(temperature: Temperature,
                                   vaporPressure: Pressure, stationPressure: Pressure): Temperature =
    (temperature in Kelvin) / (1-(vaporPressure/stationPressure)*(1-0.622))

}
