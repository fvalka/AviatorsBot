package com.vektorraum.aviatorsbot.bot.util

import org.scalatest.Matchers._
import org.scalatest.{FunSuite, GivenWhenThen}

/**
  * Created by fvalka on 23.05.2017.
  */
class AliasCommandParserTest extends FunSuite with GivenWhenThen {

  test("commands match when equal") {
    AliasCommandParser.matchCommands("cmd", "cmd") should be (true)
  }

  test("commands match when starting with /") {
    AliasCommandParser.matchCommands("/cmd", "cmd") should be (true)
  }

  test("commands dont match when different") {
    AliasCommandParser.matchCommands("cmdo", "cmd") should be (false)
  }

  test("commands dont match when different even when starting with /") {
    AliasCommandParser.matchCommands("/cmd", "ocmd") should be (false)
  }

}
