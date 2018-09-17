package com.vektorraum.aviatorsbot.persistence.sigmets

import java.util.Date

import com.vektorraum.aviatorsbot.persistence.DatabaseSetup
import com.vektorraum.aviatorsbot.persistence.sigmets.model.SigmetInfo
import org.scalatest.Matchers._
import org.scalatest.{AsyncFeatureSpec, GivenWhenThen}

class SigmetInfoDAOProductionIT
  extends AsyncFeatureSpec
    with GivenWhenThen
    with DatabaseSetup {
  info("As a pilot I want to")
  info("be able to get a map of all SIGMETs and AIRMETs but not receive all the SIGMETs at once")
  info("by storing it in a database I can retrieve only the ones I need later")

  val dao: SigmetInfoDAOProduction = new SigmetInfoDAOProduction(db)

  feature("Storage of SigmetInfos") {
    scenario("SigmetInfo is set and then retrieved from the database") {
      Given("A new SimgetInfo")
      val input1 = SigmetInfo(1234L, 1, "Testinfo")
      val input = Seq(input1)

      When("Storing the sigmetinfo in the database and then getting it")
      cleanDb flatMap { _ =>
        dao.store(input)
      } flatMap { res =>
        res.ok shouldEqual true
        dao.get(input1.chatId, input1.index)
      } map { res =>
        res should not be empty
        res.get shouldEqual input1
      }
    }

    scenario("Old sigmets time out") {
      Given("An expired SimgetInfo")
      val input1 = SigmetInfo(1234L, 1, "Testinfo", new Date())
      val input = Seq(input1)

      When("Storing the sigmetinfo in the database and then getting it")
      cleanDb flatMap { _ =>
        dao.store(input)
      } flatMap { res =>
        res.ok shouldEqual true
        dao.get(input1.chatId, input1.index)
      } map { res =>
        res shouldBe empty
      }
    }

    scenario("Multiple sigmets are stored correctly") {
      Given("Multiple SigmetInfos")
      val input1 = SigmetInfo(1234L, 1, "Testinfo")
      val input2 = SigmetInfo(1234L, 2, "A totaly different testinfo")
      val input = Seq(input1, input2)

      When("Storing the sigmetinfo in the database and then getting it")
      cleanDb flatMap { _ =>
        dao.store(input)
      } flatMap { res =>
        res.ok shouldEqual true
        dao.get(input1.chatId, input1.index)
      } flatMap { res =>
        res should not be empty
        res.get shouldEqual input1
        dao.get(input1.chatId, input2.index)
      } map { res =>
        res should not be empty
        res.get shouldEqual input2
      }
    }

    scenario("Storing new sigmets replaces all old ones") {
      Given("Old and new SIGMETs")
      val old1 = SigmetInfo(1234L, 1, "Testinfo")
      val old2 = SigmetInfo(1234L, 2, "A totaly different testinfo")
      val old3 = SigmetInfo(1234L, 3, "Another old sigmet")
      val old = Seq(old1, old2, old3)

      val new1 = SigmetInfo(1234L, 1, "Absolutely not the old one")
      val new2 = SigmetInfo(1234L, 2, "New again")
      val newInfos = Seq(new1, new2)

      When("Storing the old ones and than the new ones")
      cleanDb flatMap { _ =>
        dao.store(old)
      } flatMap { res =>
        res.ok shouldEqual true
        dao.get(old3.chatId, old3.index)
      } flatMap { res =>
        res should not be empty
        res.get shouldEqual old3
        dao.store(newInfos)
      } flatMap { res =>
        res.ok shouldEqual true
        dao.get(old3.chatId, old3.index)
      } map { res =>
        res shouldBe empty
      }
    }
  }

}
