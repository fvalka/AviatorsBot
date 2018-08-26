package com.vektorraum.aviatorsbot.bot.weather

import com.vektorraum.aviatorsbot.bot.util.StationUtil
import com.vektorraum.aviatorsbot.generated.metar.METAR
import com.vektorraum.aviatorsbot.generated.taf.TAF

import scala.collection.mutable

object BuildWxMessage {

  /**
    * Builds and formats a weather message with a list of stations, METARs and TAFs.
    *
    * The METARs and TAFs are included in the correct order as per the station list
    * and wildcards are supported.
    *
    * Stations, the sequence of stations, is also used to include no METAR/TAF received messages
    * in the response.
    *
    * @param stations Sequence of stations, used for correct ordering
    * @param metars Sequence of METARs for the stations
    * @param tafs Sequence of TAFS for the stations
    * @return
    */
  def apply(stations: Seq[String],
             metars: Map[String, Seq[METAR]],
             tafs: Map[String, Seq[TAF]]): String = {
    // Support for wild-cards in station lists
    val inputStationsSet = mutable.LinkedHashSet(stations.filter(StationUtil.isICAOAptIdentifier): _*)
    val stationSet = inputStationsSet ++ metars.keySet

    val result = stationSet.map(station => {
      val metar = metars.get(station) match {
        case Some(mt) => FormatMetar(mt)
        case None => s"<strong>$station</strong> No METAR received for station"
      }
      val taf = tafs.get(station) match {
        case Some(tf) => FormatTaf(tf)
        case None => s"<strong>TAF $station</strong> No TAF received for station"
      }
      metar + "\n" + taf
    }) mkString "\n"

    if(result.isEmpty) {
      "No weather received for this query. Try using an ICAO station code."
    } else {
      result
    }
  }

}
