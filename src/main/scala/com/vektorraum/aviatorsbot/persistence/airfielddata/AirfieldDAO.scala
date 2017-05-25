package com.vektorraum.aviatorsbot.persistence.airfielddata

import com.vektorraum.aviatorsbot.persistence.Db
import com.vektorraum.aviatorsbot.persistence.airfielddata.model.{Airfield, Runway}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, Macros}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Created by fvalka on 25.05.2017.
  */
object AirfieldDAO {
  def airfieldCollection: Future[BSONCollection] = Db.aviatorsDb.map(_.collection("airfields"))
  implicit def runwayReader: BSONDocumentReader[Runway] = Macros.reader[Runway]
  implicit def airfieldsReader: BSONDocumentReader[Airfield] = Macros.reader[Airfield]

  def findByIcao(icao: String): Future[Option[Airfield]] = {
    val query = BSONDocument("icao" -> icao)
    airfieldCollection.flatMap(_.find(query).one[Airfield])
  }

}