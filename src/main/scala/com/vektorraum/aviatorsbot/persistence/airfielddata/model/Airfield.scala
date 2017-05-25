package com.vektorraum.aviatorsbot.persistence.airfielddata.model

/**
  * Created by fvalka on 25.05.2017.
  */
case class Airfield(icao: String, name: String, runways: Seq[Runway]) {

}
