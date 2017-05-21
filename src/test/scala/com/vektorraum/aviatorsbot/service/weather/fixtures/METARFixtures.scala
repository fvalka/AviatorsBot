package com.vektorraum.aviatorsbot.service.weather.fixtures

import com.vektorraum.aviatorsbot.generated.METAR

/**
  * Created by fvalka on 20.05.2017.
  */
object METARFixtures {
  val ValidAndCompleteLOWW: METAR = scalaxb.fromXML[METAR](
    <METAR>
      <raw_text>
        LOWW 201920Z 32019KT 9999 FEW032 BKN036 16/09 Q1019 NOSIG
      </raw_text>
      <station_id>LOWW</station_id>
      <observation_time>2017-05-20T19:20:00Z</observation_time>
      <latitude>48.12</latitude>
      <longitude>16.57</longitude>
      <temp_c>16.0</temp_c>
      <dewpoint_c>9.0</dewpoint_c>
      <wind_dir_degrees>320</wind_dir_degrees>
      <wind_speed_kt>19</wind_speed_kt>
      <visibility_statute_mi>6.21</visibility_statute_mi>
      <altim_in_hg>30.088583</altim_in_hg>
      <quality_control_flags>
        <no_signal>TRUE</no_signal>
      </quality_control_flags>
      <sky_condition sky_cover="FEW" cloud_base_ft_agl="3200"/>
      <sky_condition sky_cover="BKN" cloud_base_ft_agl="3600"/>
      <flight_category>VFR</flight_category>
      <metar_type>METAR</metar_type>
      <elevation_m>190.0</elevation_m>
    </METAR>
  )

}
