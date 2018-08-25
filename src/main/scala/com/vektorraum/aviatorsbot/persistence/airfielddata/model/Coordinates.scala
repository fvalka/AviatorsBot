package com.vektorraum.aviatorsbot.persistence.airfielddata.model

/**
  * Coordinates, longitude first
  * @param lon Longitude, -180 to 180 degrees
  * @param lat Latitude, -90 to 90 degrees
  */
case class Coordinates(lon: Double, lat: Double)
