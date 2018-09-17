package com.vektorraum.aviatorsbot.persistence.sigmets
import com.vektorraum.aviatorsbot.persistence.WriteResult
import com.vektorraum.aviatorsbot.persistence.sigmets.model.SigmetInfo

import scala.concurrent.Future

/**
  * DAO for managing SIGMET information which are stored so that the user can later retrieve the
  * actual raw SIGMETs.
  */
trait SigmetInfoDAO {

  /**
    * Retrieves a SigmetInfo for a specific user from the database
    *
    * @param chatId ChatId of the user
    * @return Future optional of the SigmetInfo. None if no SigmetInfo was found for this user.
    */
  def get(chatId: Long, index: Int): Future[Option[SigmetInfo]]

  /**
    * Stores the sigmetinfos.
    *
    * WARNING! It will delete all infos for all chatIds in the input sequence
    *
    * @param sigmetInfos All SigmetInfos to be stored
    * @return Write result, ok if the store was successfull for all elements
    */
  def store(sigmetInfos: Seq[SigmetInfo]): Future[WriteResult]

  /**
    * Delete all stored SigmetInfos for a specific user
    *
    * @param chatId Users chatId for which all SigmetInfos will be deleted
    * @return WriteResult.ok if the delete was successful
    */
  def removeAll(chatId: Long): Future[WriteResult]

  /**
    * Remove all subscriptions which are no longer active (validUntil is in the past)
    *
    * @return Write result of the remove operation
    */
  def purgeOld(): Future[WriteResult]
}
