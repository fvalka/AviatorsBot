package com.vektorraum.aviatorsbot.bot.commands

/**
  * Description for a Command and its Arguments
  *
  * @param command Name of the command e.g. ls
  * @param description Description for help command
  * @param arguments All arguments this command expects
  */
case class Command(command: String, description: String, arguments: Set[Argument] = Set.empty) {

}

object Command {
  private val COMMAND_PREFIX = "/"

  /**
    * Checks if the command matches the input, arguments ignored
    *
    * @param command Command to be matched
    * @param input Complete message received
    * @return If the command in the Command object matches the command in the message
    */
  def matches(command: Command, input: String): Boolean = {
    val parts = input.split(" ")
    val cmd = parts.head.replace(COMMAND_PREFIX,"").toLowerCase

    cmd == command.command
  }

  /**
    * Verifies that the arguments are all valid and the input does not contain arguments
    * which do not match any of the commands arguments.
    *
    * @param command Command object with Arguments
    * @param input Complete message received
    * @return If all Argument constraints are fulfilled and all input parts match Command arguments
    */
  def valid(command: Command, input: String): Boolean = {
    val rawArgs = input.split(" ").drop(1)
    val argsResult = argsExtract(command, input)
    val numberOfMatchedArgs = argsResult.map(_._2.length).sum

    val argumentConstraints = command.arguments forall { argument =>
      val amount = argsResult
        .get(argument.name)
        .map(_.length)
        .getOrElse(0)

      argument.min.forall(amount >= _) && argument.max.forall(amount <= _)
    }

    argumentConstraints && rawArgs.length == numberOfMatchedArgs
  }

  /**
    * Extract the arguments from the input.
    *
    * @param command Command object used to extract argument map
    * @param input Complete message received
    * @return Map of all arguments, mapped by name
    */
  def args(command: Command, input: String): Map[String, Seq[String]] = {
    require(valid(command, input), "Input to command must be valid for argument extraction")

    argsExtract(command, input)
  }

  protected def argsExtract(command: Command, input: String): Map[String, Seq[String]] = {
    val rawArgs: Array[String] = input.split(" ").drop(1)

    //val resultMap = mutable.HashMap[String, Seq[String]]
    command.arguments flatMap { argument =>
      val matchedArgs = rawArgs flatMap { rawArg =>
        if (argument.matcher(rawArg)) {
          Some(argument.preprocessor(rawArg))
        } else {
          None
        }
      } toList

      if(matchedArgs.nonEmpty) {
        Some(argument.name -> matchedArgs)
      } else {
        None
      }
    } toMap
  }
}
