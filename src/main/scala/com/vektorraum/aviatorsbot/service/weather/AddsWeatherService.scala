package com.vektorraum.aviatorsbot.service.weather

import akka.http.scaladsl.unmarshalling.Unmarshal
import com.vektorraum.aviatorsbot.generated.{METAR, Response}

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.xml.Elem


/**
  * Created by fvalka on 19.05.2017.
  */
class AddsWeatherService {
  val MetarMaxAge = 7

  def callAddsServer(stations: List[String], maxAge: Int): Future[Elem] = {
    Future {
      blocking {
        xml.XML.load("https://aviationweather.gov/adds/dataserver_current/httpparam?" +
          "dataSource=metars&requestType=retrieve&format=xml&" +
          s"stationString=${stations mkString ","}&hoursBeforeNow=$maxAge"
        )
      }
    }
  }

  def getMetars(stations: List[String]): Future[Map[String, Seq[METAR]]] = {
    callAddsServer(stations, MetarMaxAge) map { xml =>
      scalaxb.fromXML[Response](xml)
    } map { response =>
      val metars = response.data.datasequence1.map(dataseq => dataseq.METAR)
      metars.groupBy(_.station_id.getOrElse(throw new Exception("Invalid XML, station id is not set")))
    }
  }

}
