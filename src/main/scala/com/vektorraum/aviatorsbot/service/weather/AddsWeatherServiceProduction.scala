package com.vektorraum.aviatorsbot.service.weather

import nl.grons.metrics4.scala.DefaultInstrumented

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}
import scala.xml.Elem

/**
  * Production implementation of call to NOAA Text Data Server
  *
  */
class AddsWeatherServiceProduction extends AddsWeatherService with DefaultInstrumented {
  private val metarsTimer = metrics.timer("metars")
  private val tafsTimer = metrics.timer("tafs")

  /**
    * Calls the live adds server for retrieving METARs
    *
    * @param stations List of station ICAO ids
    * @param maxAge Maximum age of the retrieved weather info in hours
    * @return Future of the XML Response Elem
    */
  override protected def callAddsServerMetar(stations: Iterable[String], maxAge: Int): Future[Elem] =
  metarsTimer.timeFuture {
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
  override protected def callAddsServerTaf(stations: Iterable[String], maxAge: Int): Future[Elem] =
  tafsTimer.timeFuture {
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
