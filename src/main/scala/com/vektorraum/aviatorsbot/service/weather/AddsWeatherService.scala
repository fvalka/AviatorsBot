package com.vektorraum.aviatorsbot.service.weather

import akka.http.scaladsl.unmarshalling.Unmarshal
import com.vektorraum.aviatorsbot.generated.metar.{METAR, Response}
import com.vektorraum.aviatorsbot.generated.taf.TAF
import com.vektorraum.aviatorsbot.generated.taf

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.xml.Elem


/**
  * Created by fvalka on 19.05.2017.
  */
trait AddsWeatherService {
  val MetarMaxAge = 7
  val TafMaxAge = 12

  protected def callAddsServerMetar(stations: List[String], maxAge: Int): Future[Elem]
  protected def callAddsServerTaf(stations: List[String], maxAge: Int): Future[Elem]

  def getMetars(stations: List[String]): Future[Map[String, Seq[METAR]]] = {
    callAddsServerMetar(stations, MetarMaxAge) map { xml =>
      scalaxb.fromXML[Response](xml)
    } map { response =>
      val metars = response.data.datasequence1.map(dataseq => dataseq.METAR)
      metars.groupBy(_.station_id.getOrElse(throw new Exception("Invalid XML, station id is not set")))
    }
  }

  def getTafs(stations: List[String]): Future[Map[String, Seq[TAF]]] = {
    callAddsServerTaf(stations, TafMaxAge) map { xml =>
      scalaxb.fromXML[taf.Response](xml)
    } map { response =>
      val tafs = response.data.datasequence1.map(dataseq => dataseq.TAF)
      tafs.groupBy(_.station_id.getOrElse(throw new Exception("Invalid XML, station id is not set")))
    }
  }



}
