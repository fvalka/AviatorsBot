package com.vektorraum.aviatorsbot.bot.weather

import com.vektorraum.aviatorsbot.generated.taf.TAF

/**
  * Created by fvalka on 23.05.2017.
  */
object FormatTaf extends ((Seq[TAF], Option[TAF], Boolean) => String) {
  def apply(history: Seq[TAF], diffTo: Option[TAF] = None, sparklines: Boolean = false): String = {
    if(history.isEmpty) {
      return ""
    }

    if(sparklines || diffTo.isDefined) {
      throw new NotImplementedError()
    }

    val raw = history.head.raw_text.get.trim
    val station_name = history.head.station_id.get

    val stationEndPos = raw.indexOf(station_name) + 4
    "<strong>"+ raw.slice(0, stationEndPos) + "</strong>" + raw.slice(stationEndPos, raw.length)
  }

}
