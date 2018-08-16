package com.vektorraum.aviatorsbot.bot.commands

import com.typesafe.scalalogging.Logger
import com.vektorraum.aviatorsbot.bot.util.HelpMessages
import info.mukel.telegrambot4s.api.declarative.Messages
import info.mukel.telegrambot4s.methods.ParseMode
import info.mukel.telegrambot4s.models.Message
import nl.grons.metrics4.scala.{DefaultInstrumented, Meter}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Provides hooks which can be used for easier handling of commands with
  * arguments.
  *
  * Additionally this trait provides logging and metrics/instrumentation for
  * all incoming messages and times the performance of each command.
  *
  */
trait InstrumentedCommands extends Messages with DefaultInstrumented {
  // LOGGING
  protected val trafficLog = Logger("traffic-log")

  // METRICS
  protected val commandMeter: Meter = metrics.meter("commands-received")
  protected val commandsInvalidArgsMeter: Meter = metrics.meter("commands-invalid-arguments")

  /**
    * Hook a command into the Telegram bot message receive mechanism
    *
    * The Map which is provided in the func contains a mapping of arguments to
    * received Strings for this argument. The key of the map is the Argument.name
    * Argument.name -> Seq of words which matched the Argument.matcher.
    *
    * Func must return a Future for instrumentation. A timer runs from the initial call to
    * func until the future completes.
    *
    * @param command Command definition, including definition of arguments
    * @param func Handles the received message and returns any kind of Future for instrumentation
    */
  def onCommand(command: Command)
               (func: Message => Map[String, Seq[String]] => Future[Any]): Unit = {
    val timer = metrics.timer(s"command-${command.command}")

    onMessage { implicit message =>
      val text = message.text.getOrElse("")
      if(Command.matches(command, text)) {
        commandMeter.mark()

        if(Command.valid(command, text)) {
          logger.debug(s"Received valid command=$command in msg=$message")

          timer.timeFuture(func(message)(Command.args(command, text)))
        } else {
          logger.debug(s"Received command with invalid arguments command=$command and msg=$message")
          commandsInvalidArgsMeter.mark()

          val helpFileName = command.command
          reply(HelpMessages(helpFileName), Some(ParseMode.HTML))
        }
      }
    }
  }


}
