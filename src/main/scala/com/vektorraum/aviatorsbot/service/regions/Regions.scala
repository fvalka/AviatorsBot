package com.vektorraum.aviatorsbot.service.regions

import enumeratum.values.{StringEnum, StringEnumEntry, StringReactiveMongoBsonValueEnum}

import scala.collection.immutable

sealed abstract class Regions(val value: String, val description: String,
                              val children: Option[Seq[Regions]] = None)
  extends StringEnumEntry
    with Product
    with Serializable

object Regions extends StringEnum[Regions] with StringReactiveMongoBsonValueEnum[Regions] {
  case object Europe extends Regions("eu", "Europe")
  case object NorthAmerica extends Regions("na", "North America")
  case object SouthAmerica extends Regions("sa", "South America")
  case object Asia extends Regions("as", "Asia")
  case object Oceania extends Regions("oc", "Oceania")
  case object Africa extends Regions("af", "Africa")

  lazy val values: immutable.IndexedSeq[Regions] = findValues
}
