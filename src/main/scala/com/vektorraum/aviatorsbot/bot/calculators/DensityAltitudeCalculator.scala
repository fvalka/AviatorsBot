package com.vektorraum.aviatorsbot.bot.calculators

import com.typesafe.scalalogging.Logger
import com.vektorraum.aviatorsbot.bot.util.TimeFormatter
import com.vektorraum.aviatorsbot.calculations.{DensityAltitude, StationPressure}
import com.vektorraum.aviatorsbot.generated.metar.METAR
import com.vektorraum.aviatorsbot.persistence.airfielddata.AirfieldDAO
import com.vektorraum.aviatorsbot.service.weather.AddsWeatherService
import squants.MetricSystem
import squants.motion.{Pascals, Pressure}
import squants.space.Meters
import squants.thermal.Celsius

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DensityAltitudeCalculator(airfieldDAO: AirfieldDAO, addsWeatherService: AddsWeatherService) {
  private val logger = Logger(getClass)

  /**
    * Use the calculations package to perform the actual calculation using the values extracted from the
    * METAR
    *
    * @param metar METAR containing all values necessary for the density altitude calculation
    * @return Response message to be sent. Either density altitude result or error message
    */
  def apply(metar: METAR): Future[String] = {
    if (metar.altim_in_hg.isEmpty) {
      airfieldDAO.near(metar.station_id.getOrElse("Unknown")) flatMap {
        nearbyFields =>
          val stations = nearbyFields.map(_.icao)

          addsWeatherService.getMetars(stations) map {
            weather =>
              stations
                .flatMap(key => weather.get(key))
                .flatMap(_.headOption)
                .find(_.altim_in_hg.nonEmpty)
          } map {
            nearbyMetar =>
              val altimeterSetting = nearbyMetar.flatMap(_.altim_in_hg)
              val station = nearbyMetar.flatMap(_.station_id).getOrElse("Unknown")

              val warning = altimeterSetting.map { altimeter =>
                val altimeter_hPa = (inHgToPressure(altimeter).toPascals / MetricSystem.Hecto).round
                "\n\n<strong>WARNING!</strong> The METAR didn't contain an altimeter setting!\n" +
                  s"The altimeter setting of $station was used: $altimeter_hPa hPa"
              } getOrElse ""

              buildMessage(metar, altimeterSetting, warning)
          }
      }
    } else {
      Future.successful(buildMessage(metar, metar.altim_in_hg))
    }
  }

  private def buildMessage(metar: METAR, altimeterSetting: Option[Float], warning: String = "") = {
    val stationPressure = for {
      elevation <- metar.elevation_m
      altimeter <- altimeterSetting
    } yield StationPressure(Meters(elevation), inHgToPressure(altimeter))

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
          s"Density altitude: <strong>$da_ft ft</strong>" +
          warning
      case None => "Missing values in METAR, calculation can not be performed."
    }
  }

  /**
    * Converts inHg to squants Pressure unit
    *
    * @param in Pressure in inHg
    * @return Pressure in squants Pressure representation
    */
  private def inHgToPressure(in: Double): Pressure = {
    Pascals(in / 2.953e-4)
  }
}
