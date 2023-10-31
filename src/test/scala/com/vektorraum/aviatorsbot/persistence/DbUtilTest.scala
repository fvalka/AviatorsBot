package com.vektorraum.aviatorsbot.persistence

import org.scalatest.matchers.should.Matchers._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.GivenWhenThen
import org.scalatest.funsuite.AnyFunSuite

class DbUtilTest extends AnyFunSuite with TableDrivenPropertyChecks with GivenWhenThen {
  test("Wildcards are converted correctly") {
    Given("Various wildcard inputs")
    val properties = Table(
      ("region", "result"),
      ("LOW*", "^LOW.*"),
      ("*LO", "^.*LO"),
      ("NOWILD", "NOWILD"),
      ("?ILL", "?ILL"),
      ("", ""),
      ("TWO*WILD*", "^TWO.*WILD.*"),
      ("TWICE**", "^TWICE.*.*")
    )

    Then("DbUtil converts as expected")
    forAll (properties) { (in, result) =>
      DbUtil.wildcardToQuery(in) shouldEqual result
    }
  }

}
