package com.vektorraum.aviatorsbot.bot.util

import com.vektorraum.aviatorsbot.service.regions.Regions

object RegionUtil {
  /**
    * Determines whether the input is a valid region setting or not
    *
    * @param in Input to be checked against available regions
    * @return True if it matches a valid regions two letter code or name
    */
  def isRegion(in: String): Boolean = {
    find(in).isDefined
  }

  /**
    * Find a Region based upon either its two letter code
    *
    * @param in Input to search for
    * @return None if no matching region exists, the Region otherwise.
    */
  def find(in: String): Option[Regions] = {
    Regions.withValueOpt(in)
  }

}
