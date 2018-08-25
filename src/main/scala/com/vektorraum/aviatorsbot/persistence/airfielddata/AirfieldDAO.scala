package com.vektorraum.aviatorsbot.persistence.airfielddata

import com.vektorraum.aviatorsbot.persistence.airfielddata.model.Airfield
import squants.motion.Distance
import squants.space.Kilometers

import scala.concurrent.Future

/**
  * DAO for accessing airfield information
  */
trait AirfieldDAO {
  /**
    * Search for an airfield based upon its ICAO identifier (e.g. LOWW)
    *
    * @param icao Four letter uppercase ICAO identifier
    * @return One or no airfield with this ICAO identifier
    */
  def findByIcao(icao: String): Future[Option[Airfield]]

  /**
    * Find airports nearby another airport
    *
    * @param icao        ICAO Station code
    * @param maxDistance Maximum distance from the station
    * @return Sequence of stations within maxDistance of icao
    */
  def near(icao: String, maxDistance: Distance = Kilometers(50)): Future[Seq[Airfield]]
}
