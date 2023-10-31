package com.vektorraum.aviatorsbot.persistence.airfielddata.model

import org.scalatest.Matchers._
import org.scalatest.{FunSuite, GivenWhenThen}
import reactivemongo.bson.{BSONArray, BSONDocument}

class CoordinateReaderTest extends FunSuite with GivenWhenThen {
  test("A BSONDocument is correctly converted") {
    Given("A valid BSON Document")
    val doc = BSONDocument("type" -> "Point", "coordinates" -> BSONArray(34.12, 44.44))

    When("Reading the BSON")
    val result = CoordinateReader.read(doc)

    Then("The coordinates should be correct")
    result.lon shouldEqual 34.12
    result.lat shouldEqual 44.44
  }

}
