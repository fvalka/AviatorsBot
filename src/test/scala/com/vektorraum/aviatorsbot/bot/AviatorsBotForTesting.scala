package com.vektorraum.aviatorsbot.bot
import com.vektorraum.aviatorsbot.persistence.airfielddata.AirfieldDAO
import com.vektorraum.aviatorsbot.persistence.regions.RegionsDAO
import com.vektorraum.aviatorsbot.persistence.sigmets.SigmetInfoDAO
import com.vektorraum.aviatorsbot.persistence.subscriptions.SubscriptionDAO
import com.vektorraum.aviatorsbot.service.sigmets.SigmetService
import com.vektorraum.aviatorsbot.service.strikes.StrikesService
import com.vektorraum.aviatorsbot.service.weather.AddsWeatherService
import com.bot4s.telegram.methods.ParseMode
import com.bot4s.telegram.methods.ParseMode.ParseMode
import com.bot4s.telegram.models._
import nl.grons.metrics4.scala.FreshRegistries
import org.scalamock.scalatest.MockFactory

import scala.concurrent.{Future, Promise}

/**
  * AviatorsBot subclass used exclusively in unit tests.
  *
  * Injects stubs and mocks instead of actual backend services and records the
  * response sent to the user instead of sending it over the Telegram API.
  *
  * The response can be retrieved from the replySent field.
  *
  * Created by fvalka on 21.05.2017.
  */
class AviatorsBotForTesting(val sendMessageFailsException: Option[Throwable] = None, val ignoreMessages: Int = 0)
  extends AviatorsBot
    with MockFactory
    with FreshRegistries {

  var replySent: String = ""
  var messagesSentCount: Int = 0
  var parseMode: Option[ParseMode] = None

  private val replyPromise: Promise[String] = Promise[String]()
  val replyFuture: Future[String] = replyPromise.future
  private val parseModePromise: Promise[Option[ParseMode]] = Promise[Option[ParseMode]]()
  val parseModeFuture: Future[Option[ParseMode]] = parseModePromise.future
  private val photoSentPromise: Promise[String] = Promise[String]
  val photoSentFuture: Future[String] = photoSentPromise.future

  override lazy val weatherService: AddsWeatherService = mock[AddsWeatherService]
  override lazy val strikesService: StrikesService = mock[StrikesService]
  override lazy val sigmetService: SigmetService = mock[SigmetService]
  override lazy val sigmetInfoDAO: SigmetInfoDAO = mock[SigmetInfoDAO]
  override lazy val airfieldDAO: AirfieldDAO = mock[AirfieldDAO]
  override lazy val subscriptionDAO: SubscriptionDAO = mock[SubscriptionDAO]
  override lazy val regionsDAO: RegionsDAO = mock[RegionsDAO]

  // ensures that the bot can not connect to the real Telegram API
  override lazy val token: String = "TESTONLY"

  // Fixed values only used for testing
  val testChatId = 119771589

  override def reply(text: String, parseMode: Option[ParseMode],
                     entities: Option[List[MessageEntity]] = None,
                     disableWebPagePreview: Option[Boolean],
                     disableNotification: Option[Boolean],
                     protectContent: Option[Boolean] = None,
                     replyToMessageId: Option[Int],
                     allowSendingWithoutReply: Option[Boolean] = None,
                     replyMarkup: Option[ReplyMarkup])
                    (implicit message: Message): Future[Message] = {
    recordSentMessage(text, parseMode)
  }

  override def send(chatId: Long, text: String): Future[Message] = {
    recordSentMessage(text, parseMode)
  }

  override def sendPhoto(chatId: Long, url: String, caption: Option[String] = None): Future[Message] = {
    photoSentPromise.success(url)
    messageFuture
  }

  private def recordSentMessage(text: String, parseMode: Option[ParseMode]): Future[Message] = {
    if (messagesSentCount < ignoreMessages) {
      messagesSentCount += 1
      messageFuture
    } else {
      if (text.length > 4096) {
        throw new IllegalArgumentException("Text is too long and can not be sent to the real Telegram API!")
      }
      messagesSentCount += 1

      replySent = text
      this.parseMode = parseMode

      replyPromise.success(text)
      parseModePromise.success(parseMode)

      if (sendMessageFailsException.isDefined) {
        Future.failed(sendMessageFailsException.getOrElse(new RuntimeException))
      } else {
        messageFuture
      }
    }
  }

  private def messageFuture = {
    Future.successful {
      val chat = Chat(123L, ChatType.Private)
      Message(messageId = 123, chat = chat, date = 1234444)
    }
  }

  /**
    * Used to simulate an incoming Telegram message
    *
    * @param text Simulated text sent by the user
    */
  def receiveMockMessage(text: String): Unit = {
    val chatStub = Chat(testChatId, ChatType.Private, None, Some("test_user"), Some("Test"), Some("User"), None)
    val messageStub = Message(messageId = 75,
      from = Some(User(testChatId, isBot = false, "Test")),
      date = 1495371318,
      chat = chatStub,
      text = Some(text))
    val updateStub = Update(updateId = 329618378,
      message = Some(messageStub))

    receiveUpdate(updateStub, Option.empty)
  }

  def runSubscriptionHandler(): Unit = {
    this.subscriptionHandler.run()
  }

  /**
    * Ensures that the bot can not be started
    */
  override def run(): Future[Unit] = {
    throw new RuntimeException("The testing bot must never be started!")
  }
}
