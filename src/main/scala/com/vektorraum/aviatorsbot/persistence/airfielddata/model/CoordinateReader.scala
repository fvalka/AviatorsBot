package com.vektorraum.aviatorsbot.persistence.airfielddata.model

import reactivemongo.bson.{BSONArray, BSONDocument, BSONDocumentReader, BSONNumberLike}

/**
  * Converts a BSONDocument of a GeoJSON of coordinates into a Coordinates object
  */
object CoordinateReader extends BSONDocumentReader[Coordinates] {
  def read(bson: BSONDocument): Coordinates = {
    val result = for {
      coordinates <- bson.getAs[BSONArray]("coordinates")
      lon <- coordinates.getAs[BSONNumberLike](0).map(_.toDouble)
      lat <- coordinates.getAs[BSONNumberLike](1).map(_.toDouble)
    } yield Coordinates(lon, lat)

    // the read method is only called when there is something to read
    result.get
  }
}
