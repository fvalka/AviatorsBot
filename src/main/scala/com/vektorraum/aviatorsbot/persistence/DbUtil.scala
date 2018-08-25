package com.vektorraum.aviatorsbot.persistence

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

}
