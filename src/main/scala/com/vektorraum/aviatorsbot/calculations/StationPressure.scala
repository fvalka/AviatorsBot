package com.vektorraum.aviatorsbot.calculations

import squants.motion.{Pascals, Pressure}
import squants.space.{Length, Meters}

object StationPressure {

  /**
    * Calculates an approximation of the station pressure based upon elevation and altimeter setting
    *
    * @param elevation Stations elevation
    * @param altimeterSetting Altimeter setting, QNH
    * @return Approximated station pressure
    */
  def apply(elevation: Length, altimeterSetting: Pressure): Pressure = {
    require(altimeterSetting > Pascals(0))

    val pascalsToInchHg = 2.953e-4
    val p_inHg = altimeterSetting.to(Pascals) * pascalsToInchHg

    val base = (Meters(288) - 0.0065 * elevation.in(Meters))/Meters(288)
    val result_inHg = p_inHg * Math.pow(base, 5.2561)

    Pascals(result_inHg/pascalsToInchHg)
  }

}
