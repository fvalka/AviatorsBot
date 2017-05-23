package com.vektorraum.aviatorsbot.service.weather.fixtures

import scala.xml.Elem

/**
  * Created by fvalka on 21.05.2017.
  */
object METARResponseFixtures {

  val ValidLOWW7Hours: Elem = <response xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XML-Schema-instance" version="1.2" xsi:noNamespaceSchemaLocation="http://aviationweather.gov/adds/schema/metar1_2.xsd">
    <request_index>141238726</request_index>
    <data_source name="metars"/>
    <request type="retrieve"/>
    <errors/>
    <warnings/>
    <time_taken_ms>11</time_taken_ms>
    <data num_results="13">
      <METAR>
        <raw_text>
          LOWW 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T11:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>310</wind_dir_degrees>
        <wind_speed_kt>18</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="6000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 211120Z 31019KT 9999 FEW035 BKN048 18/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T11:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>18.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>310</wind_dir_degrees>
        <wind_speed_kt>19</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3500"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4800"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 211050Z 30019KT 9999 FEW030 BKN049 18/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T10:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>18.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>19</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3000"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4900"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 211020Z 31019KT 280V340 9999 FEW028 SCT044 BKN046 17/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T10:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>17.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>310</wind_dir_degrees>
        <wind_speed_kt>19</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="2800"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="4400"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4600"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 210950Z 31019KT 9999 FEW024 BKN044 17/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T09:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>17.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>310</wind_dir_degrees>
        <wind_speed_kt>19</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="2400"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4400"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 210920Z 30018G28KT 9999 FEW024 SCT038 BKN044 16/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T09:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>16.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>18</wind_speed_kt>
        <wind_gust_kt>28</wind_gust_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="2400"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="3800"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4400"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 210850Z 30016KT 9999 FEW036 BKN039 15/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T08:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>15.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>16</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3600"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="3900"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 210820Z 30017KT 9999 FEW044 BKN046 15/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T08:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>15.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>17</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="4400"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4600"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 210750Z 30018KT 9999 SCT042 BKN048 15/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T07:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>15.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>18</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="4200"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4800"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 210720Z 30016KT 9999 SCT042 BKN046 14/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T07:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>14.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>16</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="4200"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4600"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 210650Z 30015KT 9999 FEW032 SCT042 BKN046 14/12 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T06:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>14.0</temp_c>
        <dewpoint_c>12.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>15</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3200"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="4200"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4600"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 210620Z 30014KT 9999 FEW032 SCT042 BKN046 13/11 Q1022 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T06:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>13.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>14</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3200"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="4200"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4600"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 210550Z 30015KT 9999 FEW036 BKN040 13/11 Q1022 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T05:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>13.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>15</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3600"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
    </data>
  </response>


  val EmptyResponseForStationWhichDoesntExist: Elem = <response xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XML-Schema-instance" version="1.2" xsi:noNamespaceSchemaLocation="http://aviationweather.gov/adds/schema/metar1_2.xsd">
    <request_index>142132314</request_index>
    <data_source name="metars"/>
    <request type="retrieve"/>
    <errors/>
    <warnings/>
    <time_taken_ms>2</time_taken_ms>
    <data num_results="0"/>
  </response>

  val validLOWWandLOWG: Elem = <response xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XML-Schema-instance" version="1.2" xsi:noNamespaceSchemaLocation="http://aviationweather.gov/adds/schema/metar1_2.xsd">
    <request_index>138785354</request_index>
    <data_source name="metars"/>
    <request type="retrieve"/>
    <errors/>
    <warnings/>
    <time_taken_ms>8</time_taken_ms>
    <data num_results="28">
      <METAR>
        <raw_text>LOWW 211650Z 29017KT CAVOK 18/11 Q1021 NOSIG</raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T16:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>18.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>290</wind_dir_degrees>
        <wind_speed_kt>17</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="CAVOK"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211650Z 34010KT 9999 FEW050 SCT060 19/07 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T16:50:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>7.0</dewpoint_c>
        <wind_dir_degrees>340</wind_dir_degrees>
        <wind_speed_kt>10</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="5000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="6000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>LOWW 211620Z 30018KT CAVOK 19/11 Q1021 NOSIG</raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T16:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>18</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="CAVOK"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211620Z 34014KT 9999 FEW050 SCT060 19/07 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T16:20:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>7.0</dewpoint_c>
        <wind_dir_degrees>340</wind_dir_degrees>
        <wind_speed_kt>14</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="5000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="6000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>LOWW 211550Z 29017KT CAVOK 19/10 Q1022 NOSIG</raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T15:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>10.0</dewpoint_c>
        <wind_dir_degrees>290</wind_dir_degrees>
        <wind_speed_kt>17</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="CAVOK"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211550Z 34014KT 9999 FEW050 SCT060 20/08 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T15:50:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>20.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>340</wind_dir_degrees>
        <wind_speed_kt>14</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="5000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="6000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>LOWG 211520Z 35015KT 9999 FEW060 21/08 Q1021 NOSIG</raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T15:20:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>21.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>350</wind_dir_degrees>
        <wind_speed_kt>15</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="6000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>LOWW 211520Z 30017KT CAVOK 19/11 Q1022 NOSIG</raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T15:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>17</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="CAVOK"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211450Z 33011KT 300V360 9999 FEW060 SCT070 21/08 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T14:50:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>21.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>330</wind_dir_degrees>
        <wind_speed_kt>11</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="6000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="7000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>LOWW 211450Z 30019KT CAVOK 19/11 Q1022 NOSIG</raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T14:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>19</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="CAVOK"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211420Z 34012KT 280V360 9999 FEW060 SCT080 21/08 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T14:20:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>21.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>340</wind_dir_degrees>
        <wind_speed_kt>12</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="6000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="8000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>LOWW 211420Z 29018KT CAVOK 19/11 Q1022 NOSIG</raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T14:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>290</wind_dir_degrees>
        <wind_speed_kt>18</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="CAVOK"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>LOWW 211350Z 30017KT CAVOK 18/11 Q1022 NOSIG</raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T13:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>18.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>17</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="CAVOK"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211350Z 35016KT 9999 FEW060 SCT080 21/08 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T13:50:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>21.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>350</wind_dir_degrees>
        <wind_speed_kt>16</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="6000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="8000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>LOWW 211320Z 30018KT CAVOK 18/11 Q1022 NOSIG</raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T13:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>18.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>18</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="CAVOK"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211320Z 34014KT 300V360 9999 FEW055 SCT080 21/09 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T13:20:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>21.0</temp_c>
        <dewpoint_c>9.0</dewpoint_c>
        <wind_dir_degrees>340</wind_dir_degrees>
        <wind_speed_kt>14</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="5500"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="8000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>LOWW 211250Z 29018KT CAVOK 19/11 Q1022 NOSIG</raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T12:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>290</wind_dir_degrees>
        <wind_speed_kt>18</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="CAVOK"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211250Z 34014KT 310V010 9999 FEW055 SCT080 20/08 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T12:50:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>20.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>340</wind_dir_degrees>
        <wind_speed_kt>14</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="5500"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="8000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211220Z 34013KT 9999 FEW055 SCT080 20/08 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T12:20:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>20.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>340</wind_dir_degrees>
        <wind_speed_kt>13</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="5500"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="8000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 211220Z 30019KT 9999 FEW030 SCT080 18/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T12:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>18.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>19</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="8000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 211150Z 31018KT 9999 FEW030 SCT060 19/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T11:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>310</wind_dir_degrees>
        <wind_speed_kt>18</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="6000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211150Z 33014KT 9999 FEW055 SCT080 19/08 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T11:50:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>330</wind_dir_degrees>
        <wind_speed_kt>14</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="5500"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="8000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 211120Z 31019KT 9999 FEW035 BKN048 18/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T11:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>18.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>310</wind_dir_degrees>
        <wind_speed_kt>19</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3500"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4800"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211120Z 33014KT 9999 FEW055 SCT080 18/08 Q1022 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T11:20:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>18.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>330</wind_dir_degrees>
        <wind_speed_kt>14</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="5500"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="8000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 211050Z 30019KT 9999 FEW030 BKN049 18/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T10:50:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>18.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>300</wind_dir_degrees>
        <wind_speed_kt>19</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="3000"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4900"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211050Z 33014KT 300V020 9999 FEW060 SCT080 19/08 Q1022 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T10:50:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>330</wind_dir_degrees>
        <wind_speed_kt>14</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.177166</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="6000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="8000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWW 211020Z 31019KT 280V340 9999 FEW028 SCT044 BKN046 17/11 Q1023 NOSIG
        </raw_text>
        <station_id>LOWW</station_id>
        <observation_time>2017-05-21T10:20:00Z</observation_time>
        <latitude>48.12</latitude>
        <longitude>16.57</longitude>
        <temp_c>17.0</temp_c>
        <dewpoint_c>11.0</dewpoint_c>
        <wind_dir_degrees>310</wind_dir_degrees>
        <wind_speed_kt>19</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.206694</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="2800"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="4400"/>
        <sky_condition sky_cover="BKN" cloud_base_ft_agl="4600"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>190.0</elevation_m>
      </METAR>
      <METAR>
        <raw_text>
          LOWG 211020Z 33012KT 300V360 9999 FEW060 SCT110 19/08 Q1021 NOSIG
        </raw_text>
        <station_id>LOWG</station_id>
        <observation_time>2017-05-21T10:20:00Z</observation_time>
        <latitude>47.0</latitude>
        <longitude>15.42</longitude>
        <temp_c>19.0</temp_c>
        <dewpoint_c>8.0</dewpoint_c>
        <wind_dir_degrees>330</wind_dir_degrees>
        <wind_speed_kt>12</wind_speed_kt>
        <visibility_statute_mi>6.21</visibility_statute_mi>
        <altim_in_hg>30.147638</altim_in_hg>
        <quality_control_flags>
          <no_signal>TRUE</no_signal>
        </quality_control_flags>
        <sky_condition sky_cover="FEW" cloud_base_ft_agl="6000"/>
        <sky_condition sky_cover="SCT" cloud_base_ft_agl="11000"/>
        <flight_category>VFR</flight_category>
        <metar_type>METAR</metar_type>
        <elevation_m>347.0</elevation_m>
      </METAR>
    </data>
  </response>
}
