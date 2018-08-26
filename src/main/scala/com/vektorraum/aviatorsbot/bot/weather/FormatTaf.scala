package com.vektorraum.aviatorsbot.bot.weather

import com.vektorraum.aviatorsbot.generated.taf.TAF

/**
  * Created by fvalka on 23.05.2017.
  */
object FormatTaf extends (Seq[TAF] => String) {
  def apply(history: Seq[TAF]): String = {
    if(history.isEmpty) {
      return ""
    }

    val raw = history.head.raw_text.get.trim
    val station_name = history.head.station_id.get

    val stationEndPos = raw.indexOf(station_name) + 4
    "<strong>"+ raw.slice(0, stationEndPos) + "</strong>" + raw.slice(stationEndPos, raw.length)
  }

}
