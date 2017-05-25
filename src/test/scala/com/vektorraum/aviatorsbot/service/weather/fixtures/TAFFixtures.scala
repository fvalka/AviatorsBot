package com.vektorraum.aviatorsbot.service.weather.fixtures

import com.vektorraum.aviatorsbot.generated.taf.TAF

/**
  * Created by fvalka on 25.05.2017.
  */
object TAFFixtures {
  val ValidAndCompleteLOWW: TAF = scalaxb.fromXML[TAF](<TAF>
    <raw_text>
      TAF LOWW 231715Z 2318/2424 18004KT CAVOK TX22/2318Z TN12/2500Z FM240300 29015G25KT 9999 BKN040 TEMPO 2403/2408 30018G30KT 6000 SHRA FEW030 FEW030CB BKN040 PROB30 2404/2407 4000 TSRA FM241000 32015G25KT CAVOK TEMPO 2410/2416 33022G32KT BECMG 2416/2418 34012KT
    </raw_text>
    <station_id>LOWW</station_id>
    <issue_time>2017-05-23T17:15:00Z</issue_time>
    <bulletin_time>2017-05-23T17:00:00Z</bulletin_time>
    <valid_time_from>2017-05-23T18:00:00Z</valid_time_from>
    <valid_time_to>2017-05-25T00:00:00Z</valid_time_to>
    <latitude>48.12</latitude>
    <longitude>16.57</longitude>
    <elevation_m>190.0</elevation_m>
    <forecast>
      <fcst_time_from>2017-05-23T18:00:00Z</fcst_time_from>
      <fcst_time_to>2017-05-24T03:00:00Z</fcst_time_to>
      <wind_dir_degrees>180</wind_dir_degrees>
      <wind_speed_kt>4</wind_speed_kt>
      <visibility_statute_mi>6.21</visibility_statute_mi>
      <sky_condition sky_cover="NSC"/>
    </forecast>
    <forecast>
      <fcst_time_from>2017-05-24T03:00:00Z</fcst_time_from>
      <fcst_time_to>2017-05-24T08:00:00Z</fcst_time_to>
      <change_indicator>TEMPO</change_indicator>
      <wind_dir_degrees>300</wind_dir_degrees>
      <wind_speed_kt>18</wind_speed_kt>
      <wind_gust_kt>30</wind_gust_kt>
      <visibility_statute_mi>3.73</visibility_statute_mi>
      <wx_string>SHRA</wx_string>
      <sky_condition sky_cover="FEW" cloud_base_ft_agl="3000"/>
      <sky_condition sky_cover="FEW" cloud_base_ft_agl="3000" cloud_type="CB"/>
      <sky_condition sky_cover="BKN" cloud_base_ft_agl="4000"/>
    </forecast>
    <forecast>
      <fcst_time_from>2017-05-24T03:00:00Z</fcst_time_from>
      <fcst_time_to>2017-05-24T10:00:00Z</fcst_time_to>
      <change_indicator>FM</change_indicator>
      <wind_dir_degrees>290</wind_dir_degrees>
      <wind_speed_kt>15</wind_speed_kt>
      <wind_gust_kt>25</wind_gust_kt>
      <visibility_statute_mi>6.21</visibility_statute_mi>
      <not_decoded>TX22/2318Z TN12/2500Z</not_decoded>
      <sky_condition sky_cover="BKN" cloud_base_ft_agl="4000"/>
    </forecast>
    <forecast>
      <fcst_time_from>2017-05-24T04:00:00Z</fcst_time_from>
      <fcst_time_to>2017-05-24T07:00:00Z</fcst_time_to>
      <change_indicator>PROB</change_indicator>
      <probability>30</probability>
      <visibility_statute_mi>2.49</visibility_statute_mi>
      <wx_string>TSRA</wx_string>
    </forecast>
    <forecast>
      <fcst_time_from>2017-05-24T10:00:00Z</fcst_time_from>
      <fcst_time_to>2017-05-24T16:00:00Z</fcst_time_to>
      <change_indicator>FM</change_indicator>
      <wind_dir_degrees>320</wind_dir_degrees>
      <wind_speed_kt>15</wind_speed_kt>
      <wind_gust_kt>25</wind_gust_kt>
      <visibility_statute_mi>6.21</visibility_statute_mi>
      <wx_string>NSW</wx_string>
      <sky_condition sky_cover="NSC"/>
    </forecast>
    <forecast>
      <fcst_time_from>2017-05-24T10:00:00Z</fcst_time_from>
      <fcst_time_to>2017-05-24T16:00:00Z</fcst_time_to>
      <change_indicator>TEMPO</change_indicator>
      <wind_dir_degrees>330</wind_dir_degrees>
      <wind_speed_kt>22</wind_speed_kt>
      <wind_gust_kt>32</wind_gust_kt>
    </forecast>
    <forecast>
      <fcst_time_from>2017-05-24T16:00:00Z</fcst_time_from>
      <fcst_time_to>2017-05-25T00:00:00Z</fcst_time_to>
      <change_indicator>BECMG</change_indicator>
      <time_becoming>2017-05-24T18:00:00Z</time_becoming>
      <wind_dir_degrees>340</wind_dir_degrees>
      <wind_speed_kt>12</wind_speed_kt>
      <visibility_statute_mi>6.21</visibility_statute_mi>
      <wx_string>NSW</wx_string>
      <sky_condition sky_cover="NSC"/>
    </forecast>
  </TAF>)

}
