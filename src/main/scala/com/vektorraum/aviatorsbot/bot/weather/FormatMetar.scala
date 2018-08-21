package com.vektorraum.aviatorsbot.bot.weather

import com.vektorraum.aviatorsbot.generated.metar.METAR


/**
  * Created by fvalka on 20.05.2017.
  */
object FormatMetar extends (Seq[METAR] => String) {
  def apply(history: Seq[METAR]): String = {
    if(history.isEmpty) {
      return ""
    }

    val raw_without_station = history.head.raw_text.get.trim.substring(4).trim
    val emoji = flightCategoryToEmoji(history.head.flight_category.getOrElse("UNK"))
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
