package com.vektorraum.aviatorsbot.persistence.airfielddata

import com.vektorraum.aviatorsbot.persistence.Db
import com.vektorraum.aviatorsbot.persistence.airfielddata.model.{Airfield, CoordinateReader, Coordinates, Runway}
import reactivemongo.api.Cursor
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONArray, BSONDocument, BSONDocumentReader, Macros}
import squants.motion.Distance
import squants.space.Kilometers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * DAO for accessing airfield information
  */
class AirfieldDAOProduction(db: Db) extends AirfieldDAO {
  def airfieldCollection: Future[BSONCollection] = db.aviatorsDb.map(_.collection("airfields"))
  implicit def runwayReader: BSONDocumentReader[Runway] = Macros.reader[Runway]
  implicit def airfieldsReader: BSONDocumentReader[Airfield] = Macros.reader[Airfield]
  implicit def coordinatesReader: BSONDocumentReader[Coordinates] = CoordinateReader

  /**
    * Search for an airfield based upon its ICAO identifier (e.g. LOWW)
    *
    * @param icao Four letter uppercase ICAO identifier
    * @return One or no airfield with this ICAO identifier
    */
  override def findByIcao(icao: String): Future[Option[Airfield]] = {
    val query = BSONDocument("icao" -> icao.toUpperCase)
    airfieldCollection.flatMap(_.find(query).one[Airfield])
  }

  /**
    * Find airports nearby another airport
    *
    * @param icao ICAO Station code
    * @param maxDistance Maximum distance from the station
    * @return Sequence of stations within maxDistance of icao
    */
  override def near(icao: String, maxDistance: Distance = Kilometers(50)): Future[Seq[Airfield]] = {
    val errorHandler = Cursor.FailOnError[List[Airfield]]()

    findByIcao(icao) flatMap { field =>
      field map { airfield =>
        val coordinates = airfield.coordinates.get
        val query = BSONDocument("coordinates" ->
          BSONDocument("$near" ->
            BSONDocument("$geometry" ->
              BSONDocument("type" -> "Point",
                "coordinates" -> BSONArray(coordinates.lon, coordinates.lat)),
              "$maxDistance" -> maxDistance.toMeters
            )
          )
        )
        airfieldCollection.flatMap(_.find(query).cursor[Airfield]().collect[List](-1, errorHandler))
      } getOrElse Future.successful(Seq.empty)
    }
  }

}
