package com.vektorraum.aviatorsbot.persistence.airfielddata.model

/**
  * Airfield information
  */
case class Airfield(icao: String, name: String, magVar: Double, runways: Seq[Runway],
                    coordinates: Option[Coordinates] = None)
