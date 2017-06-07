package com.vektorraum.aviatorsbot.bot
import com.vektorraum.aviatorsbot.persistence.airfielddata.{AirfieldDAO, AirfieldDAOProduction}
import com.vektorraum.aviatorsbot.persistence.subscriptions.SubscriptionDAO
import com.vektorraum.aviatorsbot.service.weather.AddsWeatherService
import com.vektorraum.aviatorsbot.service.weather.fixtures.METARResponseFixtures
import com.vektorraum.aviatorsbot.service.weather.mocks.AddsWeatherServiceForTest
import info.mukel.telegrambot4s.methods.ParseMode.ParseMode
import info.mukel.telegrambot4s.models._
import org.scalamock.scalatest.MockFactory

import scala.concurrent.Future
import scala.xml.Elem

/**
  * Created by fvalka on 21.05.2017.
  */
class AviatorsBotForTesting(weatherServiceStub: AddsWeatherService) extends AviatorsBot with MockFactory {
  var replySent: String = ""
  var parseMode: Option[ParseMode] = None
  override lazy val weatherService: AddsWeatherService = weatherServiceStub
  override lazy val airfieldDAO: AirfieldDAO = stub[AirfieldDAO]
  override lazy val subscriptionDAO: SubscriptionDAO = mock[SubscriptionDAO]
  override lazy val token: String = "TESTONLY"

  override def reply(text: String,
                     parseMode: Option[ParseMode] = None,
                     disableWebPagePreview: Option[Boolean] = None,
                     disableNotification: Option[Boolean] = None,
                     replyToMessageId: Option[Long] = None,
                     replyMarkup: Option[ReplyMarkup] = None)
                    (implicit message: Message): Future[Message] = {
    if (replySent != "") {
      throw new IllegalArgumentException()
    }
    replySent = text
    this.parseMode = parseMode
    Future {
      val chat = Chat(123L, ChatType.Private)
      Message(messageId = 123L, chat = chat, date = 1234444)
    }
  }

  def receiveMockMessage(text: String): Unit = {
    val chatStub = Chat(119771589, ChatType.Private, None, Some("test_user"), Some("Test"), Some("User"), None)
    val messageStub = Message(messageId = 75,
      from = Some(User(119771589, "Test")),
      date = 1495371318,
      chat = chatStub,
      text = Some(text))
    val updateStub = Update(updateId = 329618378,
      message = Some(messageStub))

    onUpdate(updateStub)
  }
}
