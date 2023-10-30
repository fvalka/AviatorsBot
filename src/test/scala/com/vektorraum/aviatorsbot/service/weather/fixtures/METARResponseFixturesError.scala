package com.vektorraum.aviatorsbot.service.weather.fixtures

import scala.xml.Elem

object METARResponseFixturesError {
  val EmptyResponseForStationWhichDoesntExist: Elem = <response xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XML-Schema-instance" version="1.2" xsi:noNamespaceSchemaLocation="http://aviationweather.gov/adds/schema/metar1_2.xsd">
    <request_index>142132314</request_index>
    <data_source name="metars"/>
    <request type="retrieve"/>
    <errors/>
    <warnings/>
    <time_taken_ms>2</time_taken_ms>
    <data num_results="0"/>
  </response>
}
