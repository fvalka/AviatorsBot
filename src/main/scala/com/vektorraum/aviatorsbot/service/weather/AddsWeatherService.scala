package com.vektorraum.aviatorsbot.service.weather

import com.vektorraum.aviatorsbot.generated.metar.{METAR, Response}
import com.vektorraum.aviatorsbot.generated.taf
import com.vektorraum.aviatorsbot.generated.taf.TAF
import nl.grons.metrics4.scala.{DefaultInstrumented, Timer}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.xml.Elem


/**
  * Weather service for retrieving METARs and TAFs from the NOAA
  * aviationweather.gov Text Data Server
  */
trait AddsWeatherService {
  val MetarMaxAge = 7
  val TafMaxAge = 7

  protected def callAddsServerMetar(stations: Iterable[String], maxAge: Int): Future[Elem]
  protected def callAddsServerTaf(stations: Iterable[String], maxAge: Int): Future[Elem]

  /**
    * Gets the METARs for the last hours
    *
    * @param stations List of station ICAO ids
    * @return Future of a map ICAO -> METARs
    */
  def getMetars(stations: Iterable[String]): Future[Map[String, Seq[METAR]]] = {
    callAddsServerMetar(stations, MetarMaxAge) map { xml =>
      scalaxb.fromXML[Response](xml)
    } map { response =>
      val metars = response.data.datasequence1.map(dataseq => dataseq.METAR)
      metars.groupBy(_.station_id.getOrElse(throw new Exception("Invalid XML, station id is not set")))
    }
  }

  /**
    * Gets the TAFS for the last hours
    *
    * @param stations List of station ICAO ids
    * @return Future of a map ICAO -> TAFs
    */
  def getTafs(stations: Iterable[String]): Future[Map[String, Seq[TAF]]] = {
    callAddsServerTaf(stations, TafMaxAge) map { xml =>
      scalaxb.fromXML[taf.Response](xml)
    } map { response =>
      val tafs = response.data.datasequence1.map(dataseq => dataseq.TAF)
      tafs.groupBy(_.station_id.getOrElse(throw new Exception("Invalid XML, station id is not set")))
    }
  }



}
