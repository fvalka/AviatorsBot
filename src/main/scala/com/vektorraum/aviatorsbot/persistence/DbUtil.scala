package com.vektorraum.aviatorsbot.persistence

import com.vektorraum.aviatorsbot.persistence
import reactivemongo.api.commands

/**
  * Database utility for conversions
  */
object DbUtil {
  /**
    * Converts an input containing a * which will be replaced into a valid db query
    *
    * @param input Input to be converted into database query
    * @return Valid database query for $regex
    */
  def wildcardToQuery(input: String): String = {
    if(input.contains("*")) {
      val regex = input.replace("*", ".*")
      s"^$regex"
    } else {
      input
    }
  }

  /**
    * Converts a database writeResult into an encapsulated one
    *
    * @param original ReactiveMongo WriteResult
    * @return Encapsulated WriteResult
    */
  def convertWriteResult(original: commands.WriteResult): WriteResult = {
    persistence.WriteResult(original.ok)
  }

  /**
    * Converts a database writeResult into an encapsulated one
    *
    * @param original ReactiveMongo WriteResult
    * @return Encapsulated WriteResult
    */
  def convertWriteResult(original: commands.MultiBulkWriteResult): WriteResult = {
    persistence.WriteResult(original.ok)
  }

}
