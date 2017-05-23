package com.vektorraum.aviatorsbot.service.weather

import scala.concurrent.{Future, blocking}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.Elem

/**
  * Created by fvalka on 21.05.2017.
  */
class AddsWeatherServiceProduction extends AddsWeatherService {
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
