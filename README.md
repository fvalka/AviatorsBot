[![Build Status](https://travis-ci.org/fvalka/AviatorsBot.svg?branch=master)](https://travis-ci.org/fvalka/AviatorsBot)
[![codecov](https://codecov.io/gh/fvalka/AviatorsBot/branch/master/graph/badge.svg)](https://codecov.io/gh/fvalka/AviatorsBot)

<p align="center" style="margin-bottom: 10em;">
  <img src="doc/images/logo.png" alt="AviatorsBot"/>
</p>

This Telegram bot provides a METAR and TAF aviation weather 
subscription service and weather based tools for flight planing, 
supplementing official briefing sources.

___ 
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

THIS SOFTWARE IS NOT AN OFFICIAL BRIEFING SOURCE. ANY DATA SENT MIGHT BE WRONG, OUT OF DATE OR OTHERWISE UNUSABLE OR MISLEADING, NO GUARANTEES CAN BE MADE ABOUT THE AVAILABILITY OF THIS SERVICE, ESPECIALLY THE POLLING/SUBSCRIPTION MECHANISM
USE PURELY AT YOUR OWN RISK!
___

## Using the Bot
AviatorsBot is available on Telegram under the name: 
[`@AviatorsBot`](https://t.me/AviatorsBot)

Sending `/help` provides a list of all available commands and `/help <command>` 
will show you how to use the command. 

## Features
### METAR and TAF Subscriptions
Using this bots subscription service you will receive an update
once a new METAR or TAF is published until your subscription expires. 

Managing these subscriptions is very straight forward, user friendly
and analogous to a unix command line tool. Using `/add <ICAO>` you 
can subscribe to one or more stations. `/ls` lists the stations you
are subscribed to and `/rm` will let you unsubscribe from a station. 

### Crosswind Calculation
Sending the command `/xwind <ICAO>` to the bot will provide you with
the crosswind and headwind for all runways of that airport. 

### Density Altitude Calculation
Using the `/da <ICAO>` command the current density altitude is calculated.

This calculation is based upon the altimeter setting, elevation, temperature 
and dew point. All of these data points are taken from the METAR and require
no further user input. 

Calculating the density altitude with consideration for the actual pressure 
and water vapor pressure results in a more accurate result than just using 
the elevation and temperature. 

If the stations METAR doesn't contain an altimeter setting the METARs for the
airfields within 50km of the station will also be retrieved and the altimeter
setting of the closest field with a valid value is used. 

### Current Weather
The `/wx <ICAO>` command returns the current METAR and TAF and 
enhances the METAR with an icon for the flight category provided by 
the aviationweather.gov API. 

### Command Shortcuts
For easier typing on mobile phones you can also omit the '/' prefix.
For Example both `/help` and just sending `help` work. 

## Technical
### System Architecture Overview
MongoDB is used as the persistence backend. Weather updates are 
retrieved from the [`aviationweather.gov/NOAA ADDS Textserver`](
https://www.aviationweather.gov/dataserver) in XML format. 
Crosswind calculations are based upon the airfield and runway information 
in the [`openAIP`](https://www.openaip.net/) database. 

![System overview](doc/images/system-overview.png "System overview")

### Technology Stack
AviatorsBot is written in Scala using akka for non-blocking IO. 

MongoDB is used as the backend. Accessed using the 
[`reactive-mongo`](http://reactivemongo.org/) Scala library for extending 
the non-block and reactive concept also to this part. 

### Continuous Deployment 
Using a Amazon AWS CodePipeline the bot is continuously deployed to 
an EC2 instance. 

Code is stored in AWS CodeCommit, built with AWS CodeBuild and deployed to
the EC2 instance as a Debian dpkg package using AWS CodeDeploy if the build 
as well as all unit and integration tests succeed. 

### Production Ready, High Code Quality
The codebase has highly relevant unit and integration tests and a 
high coverage written mostly using ScalaTests FunSuite which doubles
as a specification of the bots feature set and error handling strategies.

To enable seamless testing a macro based DI library is used for 
Inversion of Control. 

### Scheduling of Subscription Updates 
[`Quartz scheduler`](http://www.quartz-scheduler.org) is used to provide reliable 
and fault tolerant scheduling of weather updates. 

For the purpose of immediate retries a RetriableException has been implemented which 
will cause an immediate refiring of the scheduler up to the number of maximum retries. 

### Logging and Metrics
Extensive logging and configurability add to the production readiness 
of the bot. 

All commands, messages received and sent and many errors are also monitored
and timed using 
[`dropwizard metrics for scala`](https://github.com/erikvanoosten/metrics-scala). 

### METAR and TAF Change Detection 
METARs and TAFs are considered changed if the MurmurHash3 of the raw text 
changes or for METARs the observation time and for TAFs the issue time. 

This means that the user will get an update if either the issuance time
changes or the content. 

MurmurHash3 is used because a cryptographic hash function is not necessary
for this application. It generates only a 32bit hash which requires for less
storage space is fast and available in the scala standard libraries. 
