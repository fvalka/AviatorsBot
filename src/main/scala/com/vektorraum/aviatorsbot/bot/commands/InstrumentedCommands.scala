package com.vektorraum.aviatorsbot.bot.commands

import com.typesafe.scalalogging.Logger
import com.vektorraum.aviatorsbot.bot.util.{HelpMessages, StationUtil}
import info.mukel.telegrambot4s.api.declarative.Messages
import info.mukel.telegrambot4s.methods.ParseMode.ParseMode
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models.{InputFile, Message, ReplyMarkup}
import nl.grons.metrics4.scala.{DefaultInstrumented, Meter, Timer}

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Provides hooks which can be used for easier handling of commands with
  * arguments.
  *
  * Additionally this trait provides logging and metrics/instrumentation for
  * all incoming messages and times the performance of each command.
  *
  */
trait InstrumentedCommands extends Messages with DefaultInstrumented {
  implicit val OrderingCommand: Ordering[Command] = Ordering.by((_: Command).command)

  // COMMAND REGISTRY
  protected type CommandFunction = Message => Map[String, Seq[String]] => Future[Any]
  protected val commandRegistry: mutable.SortedMap[Command, CommandFunction] =
    new mutable.TreeMap[Command, CommandFunction]()(OrderingCommand)
  protected val commandTimers: mutable.SortedMap[Command, Timer] = new mutable.TreeMap[Command, Timer]()

  // LOGGING
  protected val trafficLog = Logger("traffic-log")

  // METRICS
  protected val commandMeter: Meter = metrics.meter("commands-received")
  protected val commandsInvalidArgsMeter: Meter = metrics.meter("commands-invalid-arguments")
  protected val messagesReceived: Meter = metrics.meter("messages-received")
  protected val messagesSent: Meter = metrics.meter("messages-sent")

  // HELP COMMAND
  protected val helpCommand: Command = Command("help", "Overview of commands", "Info",
    Set(Argument("any", _ => true)))

  onMessage { implicit message =>
    trafficLog.info(s"Inbound chatId=${message.chat.id} - chatUserName=${message.chat.username} - " +
      s"message=${message.text} - messageId=${message.messageId}")
    messagesReceived.mark()
  }

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
  def onCommand(command: Command)(func: CommandFunction): Unit = {
    commandTimers += (command -> metrics.timer(s"command-${command.command}"))
    commandRegistry += (command -> func)
  }

  onMessage { implicit message =>
    val text = message.text.getOrElse("")
    commandMeter.mark()

    val cmd = commandRegistry.keys
      .find(Command.matches(_, text))
      .getOrElse {
        if(StationUtil.isICAOAptIdentifier(text)) {
          commandRegistry.keys
            .find(_.command == "wx")
            .getOrElse(helpCommand)
        } else {
          helpCommand
        }
      }

    val func = commandRegistry(cmd)

    if(Command.valid(cmd, text)) {
      logger.debug(s"Received valid command=$cmd in msg=$message")
      if(cmd.longRunning) { sendTyping(message.chat.id) }
      commandTimers(cmd).timeFuture(func(message)(Command.args(cmd, text)))
    } else {
      logger.debug(s"Received command with invalid arguments command=$cmd and msg=$message")
      commandsInvalidArgsMeter.mark()
      val helpFileName = cmd.command
      reply(HelpMessages(helpFileName), Some(ParseMode.HTML))
    }
  }

  onCommand(helpCommand) {
    implicit message =>
      args =>
        val cmd = args.values.headOption
          .map(_.head.replace("/",""))
          .flatMap(arg => commandRegistry.keys.find(_.command == arg))

        cmd match {
          case Some(command) => reply(HelpMessages(command.command), Some(ParseMode.HTML))
          case _ => reply(commandList(), parseMode = Some(ParseMode.HTML))
        }
  }

  def commandList(): String = {
    val groupedCommands = commandRegistry.keySet.groupBy(_.group)

    groupedCommands.keySet.to[mutable.SortedSet] map { group =>
      val commandsInGroup = groupedCommands(group) map {
        command => s"/${command.command} - ${command.description}"
      } mkString "\n"
      s"<strong>$group</strong>\n$commandsInGroup"
    } mkString "\n"
  }

  /**
    * Reply to a previous message
    *
    * @param text                   Text of the message to be sent
    * @param parseMode              Optional Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in your bot's message.
    * @param disableWebPagePreview  Optional Disables link previews for links in this message
    * @param disableNotification    Optional Sends the message silently. iOS users will not receive a notification, Android users will receive a notification with no sound.
    * @param replyToMessageId       Optional If the message is a reply, ID of the original message
    * @param replyMarkup  [[info.mukel.telegrambot4s.models.InlineKeyboardMarkup]] or
    *                     [[info.mukel.telegrambot4s.models.ReplyKeyboardMarkup]] or
    *                     [[info.mukel.telegrambot4s.models.ReplyKeyboardRemove]] or
    *                     [[info.mukel.telegrambot4s.models.ForceReply]]
    *                     Optional Additional interface options.
    *                     A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to hide reply keyboard or to force a reply from the user.
    */
  override def reply(text: String, parseMode: Option[ParseMode],
                     disableWebPagePreview: Option[Boolean],
                     disableNotification: Option[Boolean],
                     replyToMessageId: Option[Int],
                     replyMarkup: Option[ReplyMarkup])
                    (implicit message: Message): Future[Message] = {
    trafficLog.info(s"Outbound reply chatId=${message.chat.id} - chatUserName=${message.chat.username} - " +
      s"text=$text - inboundMessage=${message.text} - inboundMessageId=${message.messageId}")
    messagesSent.mark()
    super.reply(text, parseMode, disableWebPagePreview, disableNotification, replyToMessageId, replyMarkup)
  }


  /**
    * Send a HTML message to a specific chatId
    *
    * @param chatId Receiver of the message
    * @param text HTML formatted text
    * @return Future of the message completed on its delivery
    */
  def send(chatId: Long, text: String): Future[Message] = {
    logger.debug(s"Sending message from AviatorsBot to chatId=$chatId with text=$text")
    trafficLog.info(s"Outbound send - chatId=$chatId - text=$text")
    messagesSent.mark()
    request(
      SendMessage(
        chatId,
        text,
        Some(ParseMode.HTML)
      )
    )
  }

  /**
    * Sends status that the bot is currently typing, letting the user know that the request is being
    * processed
    *
    * @param chatId Chat to which the typing notifaction will be sent
    * @return
    */
  def sendTyping(chatId: Long): Future[Boolean] = {
    logger.debug("Sending typing... info")
    request(
      SendChatAction(
        chatId,
        ChatAction.Typing
      )
    )
  }

  /**
    * Send a photo which will be downloaded from url by Telegram
    *
    * @param chatId Receiver of the photo
    * @param url Url under which Telegram can download the photo
    * @return Future of the sent message
    */
  def sendPhoto(chatId: Long, url: String): Future[Message] = {
    trafficLog.info(s"Outbound send - chatId=$chatId - photo=$url")
    messagesSent.mark()
    request(
      SendPhoto(
        chatId,
        InputFile(url)
      )
    )
  }


}
