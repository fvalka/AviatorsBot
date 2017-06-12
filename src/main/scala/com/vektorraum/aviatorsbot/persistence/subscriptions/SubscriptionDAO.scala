package com.vektorraum.aviatorsbot.persistence.subscriptions
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

/**
  * Created by fvalka on 28.05.2017.
  */
trait SubscriptionDAO {

  /**
    * Inserts a new subscriptions or updates the validUntil field of the already existing subscription
    *
    * @param subscription Subscription DTO, combination of chatId and icao must be unique
    * @return The result of either the update or insert operation
    */
  def addOrExtend(subscription: Subscription): Future[WriteResult]

  /**
    * Searches for a subcription using the chatId and ICAO identifier
    *
    * @param chatId
    * @param icao
    * @return One or no subscription matching the query parameters
    */
  def find(chatId: Long, icao: String): Future[Option[Subscription]]

  /**
    * Find all subscriptions for a chatId
    *
    * @param chatId Id of the chat with the user
    * @return First 100 subscriptions found for this chat id
    */
  def findAllByChatId(chatId: Long): Future[List[Subscription]]

  /**
    * Remove all subscriptions which are no longer active (validUntil is in the past)
    *
    * @return Write result of the remove operation
    */
  def purgeOld(): Future[WriteResult]
}
