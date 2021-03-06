package com.vektorraum.aviatorsbot.service.weather

import org.scalatest.Matchers._
import org.scalatest._

/**
  * Created by fvalka on 20.05.2017.
  */
class AddsWeatherServiceIT extends AsyncFunSuite with GivenWhenThen {

  test("Calling the live systems getMetars with valid stations should yield a valid result") {
    Given("The actual live addsWeatherService")
    val cut = new AddsWeatherServiceProduction()

    When("Calling getMetars for valid stations")
    val metarsFuture = cut.getMetars(List("LOWW", "KJFK"))

    Then("Get a valid looking result")
    metarsFuture map { metars =>
      metars should contain key "LOWW"
      metars should contain key "KJFK"
      metars("LOWW") should not be empty
      metars("KJFK") should not be empty
      metars("LOWW").head.observation_time should not be empty
      metars("LOWW").head.raw_text should not be empty
      metars("LOWW").head.flight_category should not be empty
    }
  }

  test("Calling the live systems getMetars with an invalid station should yield an empty list") {
    Given("The actual live addsWeatherService")
    val cut = new AddsWeatherServiceProduction()

    When("Calling getMetars with an invalid station")
    val metarsFuture = cut.getMetars(List("XXNN"))

    Then("Get an empty list is returned")
    metarsFuture map { metars =>
      assert(metars.isEmpty)
    }
  }

  test("Calling the live systems getTafs with valid stations should yield a valid result") {
    Given("The actual live addsWeatherSerivce")
    val cut = new AddsWeatherServiceProduction()

    When("Calling getTafs for valid stations")
    val tafsFuture = cut.getTafs(List("LOWW", "KJFK"))

    Then("We get a valid looking result")
    tafsFuture map { tafs =>
      tafs should contain key "LOWW"
      tafs should contain key "KJFK"
      tafs("LOWW") should not be empty
      tafs("LOWW").head.issue_time should not be empty
      tafs("LOWW").head.raw_text should not be empty
    }
  }

  test("Calling the live systems getTafs with an invalid station should yield an empty list") {
    Given("The actual live addsWeatherService")
    val cut = new AddsWeatherServiceProduction()

    When("Calling getTafs with an invalid station")
    val tafsFuture = cut.getTafs(List("XXNN"))

    Then("Get an empty list is returned")
    tafsFuture map { tafs =>
      assert(tafs.isEmpty)
    }
  }

}
