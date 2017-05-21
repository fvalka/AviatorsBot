package com.vektorraum.aviatorsbot.bot.weather

import com.vektorraum.aviatorsbot.generated.METAR

/**
  * Created by fvalka on 20.05.2017.
  */
object FormatMetar extends ((Seq[METAR], Option[METAR], Boolean) => String) {
  def apply(history: Seq[METAR], diffTo: Option[METAR] = None, sparklines: Boolean = false): String = {
    if(sparklines || diffTo.isDefined) {
      throw new NotImplementedError()
    }

    val raw_without_station = history.head.raw_text.get.trim.substring(4).trim
    val emoji = flightCategoryToEmoji(history.head.flight_category.get)
    val station_name = history.head.station_id.get
    s"<strong>$station_name</strong> $emoji $raw_without_station"
  }

  def flightCategoryToEmoji(flightCat: String): String = {
    flightCat match {
      case "VFR" => "✅"
      case "MVFR" => "⚠"
      case "IFR" => "❗"
      case "LIFR" => "‼"
      case _ => "❔"
    }

  }
}
