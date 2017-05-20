package com.vektorraum.aviatorsbot.weather

import java.util.concurrent.TimeUnit

import org.scalatest.FunSuite

import scala.concurrent._
import scala.concurrent.duration.Duration

/**
  * Created by fvalka on 20.05.2017.
  */
class AddsWeatherServiceTest extends FunSuite {

  test("testGetMetars") {
    val cut = new AddsWeatherService()
    val metarsFuture = cut.getMetars(List("LOWW", "KJFK"))
    val metars = Await.result(metarsFuture, Duration(10000, TimeUnit.MILLISECONDS))
    println(metars)
  }

}
