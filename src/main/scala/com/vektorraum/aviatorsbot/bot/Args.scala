package com.vektorraum.aviatorsbot.bot

import com.vektorraum.aviatorsbot.bot.commands.Argument
import com.vektorraum.aviatorsbot.bot.util.{MetarTafOption, RegionUtil, StationUtil, TimeUtil}

trait Args {
  // Requires at least one ICAO code and allows adds wildcards like LO*, etc.
  protected val weatherServiceStationsArgs = Set(Argument("stations", StationUtil.isValidInput,
    min = Some(1), preprocessor = _.toUpperCase))
  // Requires at lest one ICAO code and only allows actual stations e.g. LOWW, KJFK, etc.
  protected val stationsArgs = Set(Argument("stations", StationUtil.isICAOAptIdentifier,
    min = Some(1), preprocessor = _.toUpperCase))
  // Stations with wildcards at the end e.g. LOWW, LOW*, *
  protected val wildcardStationArgs = Set(Argument("stations", StationUtil.isWildcardStation,
    min = Some(1), preprocessor = _.toUpperCase))
  // Requires exactly one actual ICAO code
  protected val oneStationArgs = Set(Argument("station", StationUtil.isICAOAptIdentifier,
    min = Some(1), max = Some(1), preprocessor = _.toUpperCase))
  // Time or duration argument
  protected val oneTimeArgs = Set(Argument("time", TimeUtil.isTimeOrDuration, max = Some(1)))
  // METAR and/or TAF option for setting subscription options
  protected val metarTafArgs = Set(Argument("metartaf", MetarTafOption.valid, max = Some(1)))
  // A region setting
  protected val regionOptionalArgs = Set(Argument("region", RegionUtil.isRegion, max = Some(1)))
  // Any arguments acceptec
  protected val anyArgs = Set(Argument("any", _ => true))

}
