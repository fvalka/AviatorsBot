package com.vektorraum.aviatorsbot.bot.commands

import com.vektorraum.aviatorsbot.bot.util.StationUtil
import org.scalatest.{FunSuite, GivenWhenThen}
import org.scalatest.Matchers._

class CommandTest extends FunSuite with GivenWhenThen {
  test("Valid command matches") {
    Given("Command with one argument")
    val argument1 = Argument("station", StationUtil.isICAOAptIdentifier)
    val command = Command("test", "Only for testing", Set(argument1))

    Then("Command matches")
    Command.matches(command, "/test") shouldEqual true
  }

  test("Valid command also matches without '/' as prefix"){
    Given("Command with one argument")
    val argument1 = Argument("station", StationUtil.isICAOAptIdentifier)
    val command = Command("test", "Only for testing")

    Then("Command matches")
    Command.matches(command, "test") shouldEqual true
  }

  test("Valid command with multiple arguments matches") {
    Given("Command with multiple arguments")
    val argument1 = Argument("station", StationUtil.isICAOAptIdentifier, min = Some(2))
    val argument2 = Argument("time", "[0-9]{2}".r.pattern.matcher(_).matches, Some(1))

    val command = Command("test", "Only for testing", Set(argument1, argument2))

    Then("Arguments are all valid")
    Command.valid(command, "/test LOWW loav 02") shouldEqual true
  }

  test("To few arguments in inputs cause validation to fail") {
    Given("Command with one argument")
    val argument1 = Argument("station", StationUtil.isICAOAptIdentifier, min = Some(2))
    val command = Command("test", "Only for testing", Set(argument1))

    Then("Validation fails if only one station is provided")
    Command.valid(command, "/test LOWW") shouldEqual false
  }

  test("No argument provided causes validation to fail") {
    Given("Command with one argument")
    val argument1 = Argument("station", StationUtil.isICAOAptIdentifier, min = Some(1))
    val command = Command("test", "Only for testing", Set(argument1))

    Then("Validation fails if no argument is provided")
    Command.valid(command, "/test") shouldEqual false
  }

  test("Too many arguments causes validation to fail") {
    Given("Command with one argument")
    val argument1 = Argument("station", StationUtil.isICAOAptIdentifier, min = Some(1), max = Some(2))
    val command = Command("test", "Only for testing", Set(argument1))

    Then("Validation fails if too many arguments are provided")
    Command.valid(command, "/test lowg LoWW LOAV") shouldEqual false
  }

  test("Superfluous argument causes validation to fail") {
    Given("Command with multiple arguments")
    val argument1 = Argument("station", StationUtil.isICAOAptIdentifier, min = Some(1))

    val command = Command("test", "Only for testing", Set(argument1))

    Then("One wrong argument causes validation to fail")
    Command.valid(command, "/test LOWW 02") shouldEqual false
  }

  test("Empty arguments are valid") {
    Given("Command with empty arguments")
    val command = Command("test", "Only for testing")

    Then("An input without any arguments is valid")
    Command.valid(command, "/test") shouldEqual true
  }

  test("Argument extraction works correctly") {
    Given("Command with multiple arguments")
    val argument1 = Argument("station", StationUtil.isICAOAptIdentifier, min = Some(2))
    val argument2 = Argument("time", "[0-9]{2}".r.pattern.matcher(_).matches, Some(1))

    val command = Command("test", "Only for testing", Set(argument1, argument2))

    When("Extracting arguments")
    val result = Command.args(command, "test LOWW LOAV 02")

    Then("Arguments are extracted correctly")
    result(argument1.name) should contain only ("LOWW", "LOAV")
    result(argument2.name) should contain only "02"
    result.keySet.size shouldEqual 2
  }

  test("Extracting the args throws an exception when the input is not valid") {
    Given("Command with multiple arguments")
    val argument1 = Argument("station", StationUtil.isICAOAptIdentifier, min = Some(2))
    val argument2 = Argument("time", "[0-9]{2}".r.pattern.matcher(_).matches, Some(1))

    val command = Command("test", "Only for testing", Set(argument1, argument2))

    Then("One wrong argument causes validation to fail")
    an [IllegalArgumentException] should be thrownBy Command.args(command, "/test LOWW inv")
  }

}
