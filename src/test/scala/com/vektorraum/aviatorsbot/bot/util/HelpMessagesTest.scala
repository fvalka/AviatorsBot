package com.vektorraum.aviatorsbot.bot.util

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers._

class HelpMessagesTest extends AnyFunSuite {
  test("Loading valid help file") {
    HelpMessages("rm") should include ("usage")
  }

  test("Loading valid help message from cache") {
    val rmHelpMessage = HelpMessages("rm") // ensure that the message has already been loaded
    val rmHelpMessageCached = HelpMessages("rm")

    rmHelpMessage should equal(rmHelpMessageCached)
  }

  test("Loading non existing help file results in error message instead of help message") {
    HelpMessages("thisfiledoesntexist") shouldEqual "Error while loading help file"
  }

  test("Trying to load a dangerous file name must result in an exception") {
    an [IllegalArgumentException] should be thrownBy HelpMessages("../rm")
    an [IllegalArgumentException] should be thrownBy HelpMessages("./rm")
    an [IllegalArgumentException] should be thrownBy HelpMessages(".rm")
    an [IllegalArgumentException] should be thrownBy HelpMessages("rm with spaces")
    an [IllegalArgumentException] should be thrownBy HelpMessages("/etc/bla")
  }

}
