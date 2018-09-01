package com.vektorraum.aviatorsbot.bot.util

import com.vektorraum.aviatorsbot.service.regions.Regions
import org.scalatest.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FunSuite, GivenWhenThen}

class RegionUtilTest extends FunSuite with TableDrivenPropertyChecks with GivenWhenThen {
  test("Region validity is detected correctly") {
    Given("Various valid and invalid regions")
    val properties = Table(
      ("region", "result"),
      ("eu", true),
      ("na", true),
      ("sa", true),
      ("as", true),
      ("af", true),
      ("oc", true),
      ("ab", false),
      ("", false),
      ("eur", false),
      ("europa", false),
      ("ey", false),
      ("bu", false)
    )

    Then("Only valid ones are identified as such")
    forAll (properties) { (region, result) =>
      RegionUtil.isRegion(region) shouldEqual result
    }
  }

  test("Region finder finds the correct regions") {
    Given("Various valid and invalid regions")
    val properties = Table(
      ("region", "result"),
      ("eu", Some(Regions.Europe)),
      ("na", Some(Regions.NorthAmerica)),
      ("sa", Some(Regions.SouthAmerica)),
      ("as", Some(Regions.Asia)),
      ("af", Some(Regions.Africa)),
      ("oc", Some(Regions.Oceania)),
      ("ab", None),
      ("", None),
      ("eur", None),
      ("europa", None),
      ("ey", None),
      ("bu", None)
    )

    Then("Only valid ones are identified as such")
    forAll (properties) { (region, result) =>
      RegionUtil.find(region) shouldEqual result
    }
  }

}
