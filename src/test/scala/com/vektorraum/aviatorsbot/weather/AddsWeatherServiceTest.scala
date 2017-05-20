package com.vektorraum.aviatorsbot.weather

import org.scalatest.FunSuite

/**
  * Created by fvalka on 20.05.2017.
  */
class AddsWeatherServiceTest extends FunSuite {

  test("testGetMetars") {
    val cut = new AddsWeatherService()
    val metars = cut.getMetars(List("LOWW", "KJFK"))
    println(metars)
  }

}
