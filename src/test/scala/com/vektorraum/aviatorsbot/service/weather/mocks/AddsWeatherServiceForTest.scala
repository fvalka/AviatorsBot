package com.vektorraum.aviatorsbot.service.weather.mocks

import com.vektorraum.aviatorsbot.service.weather.AddsWeatherService

import scala.concurrent.Future
import scala.xml.Elem
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by fvalka on 21.05.2017.
  */
class AddsWeatherServiceForTest(xml: Elem) extends AddsWeatherService {
  override def callAddsServerMetar(stations: List[String], maxAge: Int): Future[Elem] = {
    Future {
      xml
    }
  }

  override protected def callAddsServerTaf(stations: List[String], maxAge: Int): Future[Elem] = {
    throw new NotImplementedError()
  }
}
