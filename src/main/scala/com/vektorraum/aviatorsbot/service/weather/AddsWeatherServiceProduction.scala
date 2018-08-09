package com.vektorraum.aviatorsbot.service.weather

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}
import scala.xml.Elem

/**
  * Created by fvalka on 21.05.2017.
  */
class AddsWeatherServiceProduction extends AddsWeatherService {
  /**
    * Calls the live adds server for retrieving METARs
    *
    * @param stations List of station ICAO ids
    * @param maxAge Maximum age of the retrieved weather info in hours
    * @return Future of the XML Response Elem
    */
  override protected def callAddsServerMetar(stations: List[String], maxAge: Int): Future[Elem] = {
    Future {
      blocking {
        xml.XML.load("https://aviationweather.gov/adds/dataserver_current/httpparam?" +
          "dataSource=metars&requestType=retrieve&format=xml&" +
          s"stationString=${stations mkString ","}&hoursBeforeNow=$maxAge"
        )
      }
    }
  }

  /**
    * Calls the live adds server for retrieving TAFs
    *
    * @param stations List of station ICAO ids
    * @param maxAge Maximum age of the retrieved weather info in hours
    * @return Future of the XML Response Elem
    */
  override protected def callAddsServerTaf(stations: List[String], maxAge: Int): Future[Elem] = {
    Future {
      blocking {
        xml.XML.load(
          "https://aviationweather.gov/adds/dataserver_current/httpparam?" +
          "dataSource=tafs&requestType=retrieve&format=xml&" +
          s"stationString=${stations mkString ","}&hoursBeforeNow=$maxAge"
        )
      }
    }
  }
}
