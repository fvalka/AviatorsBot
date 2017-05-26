package com.vektorraum.aviatorsbot.persistence.airfielddata
import com.vektorraum.aviatorsbot.persistence.airfielddata.model.Airfield

import scala.concurrent.Future

/**
  * Created by fvalka on 26.05.2017.
  */
trait AirfieldDAO {

  def findByIcao(icao: String): Future[Option[Airfield]]
}
