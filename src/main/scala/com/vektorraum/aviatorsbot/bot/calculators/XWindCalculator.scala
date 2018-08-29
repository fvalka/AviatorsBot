package com.vektorraum.aviatorsbot.bot.calculators

import com.vektorraum.aviatorsbot.generated.metar.METAR
import com.vektorraum.aviatorsbot.persistence.airfielddata.model.Airfield

import scala.collection.SortedSet

/**
  * Calculates the crosswind component based upon the provided METAR
  * and list of runways
  *
  * Created by fvalka on 25.05.2017.
  */
object XWindCalculator {
  private val DIGIT_WIDTH_SPACE = "  "
  private val VariableWindPattern = ".*(\\d{3})V(\\d{3}).*".r

  def apply(metar: METAR, airfield: Airfield): String = {
    // Variable wind is defined as 0 degrees according to
    // https://aviationweather.gov/adds/dataserver/metars/MetarFieldDescription.php
    // Should no wind direction be present, also assume variable wind
    val windDir = metar.wind_dir_degrees.getOrElse(0)
    if (windDir == 0) {
      return "⚠ Wind variable! No calculation possible."
    }

    val directions = SortedSet(airfield.runways flatMap { runway => runway.directions }: _*)

    directions map { dir =>
      def formatForWindDirection(windDir: Int): String = {
        val normalWind = metar.wind_speed_kt
          .map(windSpeed => formatOneCombo(dir, windDir, windSpeed))
          .getOrElse("")
        val gustingWind = metar.wind_gust_kt
          .map(gustSpeed => "gusting " + formatOneCombo(dir, windDir, gustSpeed))
          .getOrElse("")
        s"$normalWind $gustingWind".trim
      }

      val normalWind = formatForWindDirection(windDir)

      val variableWind =
        if (VariableWindPattern.pattern.matcher(metar.raw_text.get.trim).matches()) {
          val VariableWindPattern(windVLeft, windVRight) = metar.raw_text.get.trim

          s"varying btw ${formatForWindDirection(windVLeft.toInt)} and " +
            s"${formatForWindDirection(windVRight.toInt)} "
        } else {
          ""
        }

      val runwayNumber = Math.round((dir - airfield.magVar)/10.0)
      val runwayTextInferred = "%02d".format(runwayNumber)

      val runwayText = runwayName(airfield, dir)
        .getOrElse(runwayTextInferred)

      s"<strong>$runwayText</strong> $normalWind $variableWind".trim
    } mkString "\n"
  }

  /**
    * Find the runway name based upon the direction
    *
    * Identifiers like L, R and C are removed so that only one runway remains
    *
    * @param airfield Airfield information, containing the runways
    * @param dir Direction of the runway as per the airfield information
    * @return Runway name with L, R, C
    */
  private def runwayName(airfield: Airfield, dir: Int) = {
    airfield.runways
      .find(_.directions.exists(_ == dir))
      .map { runway =>
        val names = runway.name.split("/")

        if(names.length == 2) {
          if (dir > 180) {
            names(1)
          } else {
            names(0)
          }
        } else {
          names(0)
        }
      }
      .map(_.replaceAll("[A-Z]", ""))
      .filter{ name =>
        val runwayNumber = name.toInt
        val runwayNumberCalculated = (dir - airfield.magVar)/10.0

        Math.abs(runwayNumber % runwayNumberCalculated) <= 1.0
      }
  }

  /**
    * Calculate the crosswind and head/tailwind for one combination of runwayDirection, windDirection
    * and windSpeed
    *
    * @param runwayDirection Runway true course direction in full degrees e.g. rwy 36 = 360
    * @param windDirection Wind direction in degrees
    * @param windSpeed Wind speed
    * @return Formatted string with crosswind and head/tailwind and arrow emojis
    */
  protected def formatOneCombo(runwayDirection: Int, windDirection: Int, windSpeed: Double): String = {
    def windAndEmoji(windSpeed: Int, emojiNegative: String, emojiPositive: String): String = {
      def formatWindSpeed(speed: Int) =
        if(speed < 10) { DIGIT_WIDTH_SPACE + speed } else { speed.toString }
      if (windSpeed == 0) {
        ""
      } else if (windSpeed < 0) {
        s"$emojiNegative" + formatWindSpeed(-1 * windSpeed) + "KT"
      } else {
        s"$emojiPositive${formatWindSpeed(windSpeed)}KT"
      }
    }

    val deltaDir = math.toRadians(windDirection - runwayDirection)
    val rightXWind = math.round(math.sin(deltaDir) * windSpeed).toInt
    val headWind = math.round(math.cos(deltaDir) * windSpeed).toInt

    val xWindText = windAndEmoji(rightXWind, "➡", "⬅")
    val headWindText = windAndEmoji(headWind, "⬆", "⬇")

    s"$xWindText $headWindText".trim
  }

}
