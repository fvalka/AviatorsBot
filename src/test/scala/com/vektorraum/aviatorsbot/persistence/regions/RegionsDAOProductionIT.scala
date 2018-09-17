package com.vektorraum.aviatorsbot.persistence.regions

import com.vektorraum.aviatorsbot.persistence.DatabaseSetup
import com.vektorraum.aviatorsbot.persistence.regions.model.RegionSetting
import com.vektorraum.aviatorsbot.service.regions.Regions
import org.scalatest.{AsyncFeatureSpec, GivenWhenThen}
import org.scalatest.Matchers._

class RegionsDAOProductionIT
  extends AsyncFeatureSpec
    with GivenWhenThen
    with DatabaseSetup {
  info("As a pilot I want to")
  info("be able to set my preferred region so that I don't have to supply it every time")
  info("by storing it in a permanent backend")

  val dao: RegionsDAOProduction = new RegionsDAOProduction(db)

  feature("Storage of RegionSettings") {
    scenario("RegionSetting is set and then retrieved from the database") {
      Given("A new RegionSetting")
      val input = RegionSetting(1234L, Regions.SouthAmerica)

      When("Storing the region in the database and then getting it")
      cleanDb flatMap { _ =>
        dao.set(input)
      } flatMap { res =>
        res.ok shouldEqual true
        dao.get(input.chatId)
      } map { res =>
        res should not be empty
        res.get shouldEqual input
      }
    }

    scenario("Pilot changes his preferred region") {
      Given("An original and a new RegionSetting")
      val original = RegionSetting(1234L, Regions.NorthAmerica)
      val newSetting = RegionSetting(1234L, Regions.Asia)

      When("Storing the region in the database and then getting it")
      cleanDb flatMap { _ =>
        dao.set(original)
      } flatMap { res =>
        res.ok shouldEqual true
        dao.get(original.chatId)
      } flatMap { res =>
        res should not be empty
        res.get shouldEqual original
      } flatMap { _ =>
        dao.set(newSetting)
      } flatMap { res =>
        res.ok shouldEqual true
        dao.get(original.chatId)
      } map { res =>
        res should not be empty
        res.get shouldEqual newSetting
      }
    }

    scenario("No setting exists for the pilot yet") {
      Given("Any chatId")
      val chatId = 1234L

      When("Getting an empty setting")
      cleanDb flatMap { _ =>
        dao.get(chatId)
      } map { res =>
        res shouldBe empty
      }
    }
  }

}
