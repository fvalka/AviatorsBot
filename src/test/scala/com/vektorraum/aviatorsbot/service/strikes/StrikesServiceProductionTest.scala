package com.vektorraum.aviatorsbot.service.strikes

import com.vektorraum.aviatorsbot.service.regions.Regions
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.matchers.should.Matchers._

class StrikesServiceProductionTest extends AnyFeatureSpec with GivenWhenThen {
  feature("Strikes service finds URLs for lightning strike maps for different regions") {
    scenario("Valid region is used to request a URL from the service"){
      Given("Service instance")
      val service = new StrikesServiceProduction

      When("Requesting the URL for a valid station")
      val result = service.getUrl(Regions.Europe)

      Then("The result includes the correct url and a time parameter")
      result should not be empty
      result.get should include ("http://images.blitzortung.org/Images/image_b_eu.png")
      result.get should include ("?t=")
    }
  }

}
