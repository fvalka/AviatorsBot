package com.vektorraum.aviatorsbot.persistence.subscriptions
import com.vektorraum.aviatorsbot.persistence.subscriptions.model.Subscription

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
    * @param chatId Id of the chat with the user
    * @param icao ICAO identifier of the station
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
    * Finds all subscriptions for a specific stations ICAO code
    *
    * @param icao ICAO code of the station for which all subscribers should be found
    * @return List of subscriptions for this station
    */
  def findAllSubscriptionsForStation(icao: String): Future[List[Subscription]]

  /**
    * Find all stations which have any subscriptions
    *
    * @return A set of all ICAO codes to which at least one user has subscribed
    */
  def findAllStations(): Future[Set[String]]

  /**
    * Removes a subscription for a specific chatId and station from the
    * database.
    *
    * @param chatId Id of the chat with the user
    * @param station ICAO identifier of the station to be removed
    * @return Future of the write result
    */
  def remove(chatId: Long, station: String): Future[WriteResult]

  /**
    * Remove all subscriptions which are no longer active (validUntil is in the past)
    *
    * @return Write result of the remove operation
    */
  def purgeOld(): Future[WriteResult]
}
