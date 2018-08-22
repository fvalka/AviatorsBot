package com.vektorraum.aviatorsbot.bot.calculators

import com.vektorraum.aviatorsbot.bot.util.TimeFormatter
import com.vektorraum.aviatorsbot.calculations.{DensityAltitude, StationPressure}
import com.vektorraum.aviatorsbot.generated.metar.METAR
import squants.motion.Pascals
import squants.space.{Feet, Meters}
import squants.thermal.Celsius

object DensityAltitudeCalculator {
  /**
    * Use the calculations package to perform the actual calculation using the values extracted from the
    * METAR
    *
    * @param metar METAR containing all values necessary for the density altitude calculaton
    * @return Response message to be sent. Either density altitude result or error message
    */
  def apply(metar: METAR): String = {
    val stationPressure = for {
      elevation <- metar.elevation_m
      altimeterSetting <- metar.altim_in_hg
    } yield StationPressure(Meters(elevation), Pascals(altimeterSetting/2.953e-4))

    val densityAlt = for {
      pressure <- stationPressure
      temperature <- metar.temp_c
      dewpoint <- metar.dewpoint_c
    } yield DensityAltitude(pressure, Celsius(temperature), Celsius(dewpoint))

    densityAlt match {
      case Some(da) =>
        val observationTime = metar.observation_time.map(TimeFormatter.shortUTCDateTimeFormat).getOrElse("Unknown")
        val da_ft = da.toFeet.round
        val elevation = Meters(metar.elevation_m.get).toFeet.round
        s"METAR observation time: $observationTime\n" +
        s"Elevation: $elevation ft\n" +
        s"Density altitude: <strong>$da_ft ft</strong>"
      case None => "Missing values in METAR, calculation can not be performed."
    }
  }

}
