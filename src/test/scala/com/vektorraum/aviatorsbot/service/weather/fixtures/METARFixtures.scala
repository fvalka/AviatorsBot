package com.vektorraum.aviatorsbot.service.weather.fixtures

import com.vektorraum.aviatorsbot.generated.metar.METAR


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

  val ValidAndCompleteLOWG: METAR = scalaxb.fromXML[METAR](
    <METAR>
      <raw_text>
        LOWG 251750Z AUTO 31005KT 280V340 9999 -SHRA FEW034 BKN038 15/13 Q1012 TEMPO RA
      </raw_text>
      <station_id>LOWG</station_id>
      <observation_time>2018-08-25T17:50:00Z</observation_time>
      <latitude>47.0</latitude>
      <longitude>15.42</longitude>
      <temp_c>15.0</temp_c>
      <dewpoint_c>13.0</dewpoint_c>
      <wind_dir_degrees>310</wind_dir_degrees>
      <wind_speed_kt>5</wind_speed_kt>
      <visibility_statute_mi>6.21</visibility_statute_mi>
      <altim_in_hg>29.88189</altim_in_hg>
      <quality_control_flags>
        <auto>TRUE</auto>
      </quality_control_flags>
      <wx_string>-SHRA</wx_string>
      <sky_condition sky_cover="FEW" cloud_base_ft_agl="3400"/>
      <sky_condition sky_cover="BKN" cloud_base_ft_agl="3800"/>
      <flight_category>VFR</flight_category>
      <metar_type>METAR</metar_type>
      <elevation_m>347.0</elevation_m>
    </METAR>
  )

  object WindCases {
    val normal: METAR = scalaxb.fromXML[METAR](
      <METAR>
        <raw_text>
          LOWW 251120Z 33006KT 9999 -SHRA FEW030 BKN055 15/10 Q1020 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-25T11:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>15.0</temp_c>
        <dewpoint_c>10.0</dewpoint_c>
        <wind_dir_degrees>330</wind_dir_degrees>
        <wind_speed_kt>6</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.11811</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <wx_string>-SHRA</wx_string>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3000"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="5500"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>)

    val varying090V170: METAR = scalaxb.fromXML[METAR](
      <METAR>
        <raw_text>
          EGPI 251150Z 13014KT 090V170 9999 FEW004 16/14 Q1022
        </raw_text>
        <station_id>EGPI</station_id>
        <observation_time>2017-05-25T11:50:00Z</observation_time>
        <latitude>55.68</latitude>
        <longitude>-6.25</longitude>
        <temp_c>16.0</temp_c>
        <dewpoint_c>14.0</dewpoint_c>
        <wind_dir_degrees>130</wind_dir_degrees>
        <wind_speed_kt>14</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="400"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>18.0</elevation_m>
      </METAR>)

    val gusting: METAR = scalaxb.fromXML[METAR](
      <METAR>
        <raw_text>
          KMYP 251151Z AUTO 27037G47KT 10SM SCT060 BKN075 04/M07 A3012 RMK AO2
        </raw_text>
        <station_id>KMYP</station_id>
        <observation_time>2017-05-25T11:51:00Z</observation_time>
        <latitude>38.48</latitude>
        <longitude>-106.32</longitude>
        <temp_c>4.0</temp_c>
        <dewpoint_c>-7.0</dewpoint_c>
        <wind_dir_degrees>270</wind_dir_degrees>
        <wind_speed_kt>37</wind_speed_kt>
        <wind_gust_kt>47</wind_gust_kt>
        <visibility_statute_mi>10.0</visibility_statute_mi>
        <altim_in_hg>30.121063</altim_in_hg>
        <quality_control_flags>
          <auto>TRUE</auto>
          <auto_station>TRUE</auto_station>
        </quality_control_flags>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="6000"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="7500"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>3667.0</elevation_m>
      </METAR>)

    val fullyVariable: METAR = scalaxb.fromXML[METAR](
      <METAR>
        <raw_text>
          LOWK 251250Z VRB02KT 9999 FEW050 SCT075 20/04 Q1018 NOSIG
        </raw_text>
        <station_id>LOWK</station_id>
        <observation_time>2017-05-25T12:50:00Z</observation_time>
        <latitude>46.65</latitude>
        <longitude>14.32</longitude>
        <temp_c>20.0</temp_c>
        <dewpoint_c>4.0</dewpoint_c>
        <wind_dir_degrees>0</wind_dir_degrees>
        <wind_speed_kt>2</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.059055</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="5000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="7500"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>441.0</elevation_m>
      </METAR>)
  }

}
