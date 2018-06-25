# AviatorsBot

This Telegram bot provides a METAR and TAF aviation weather 
subscription service and weather based tools for flight planing, 
supplementing official briefing sources.

--- 
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

THIS SOFTWARE IS NOT AN OFFICIAL BRIEFING SOURCE. ANY DATA SENT MIGHT BE WRONG, OUT OF DATE OR OTHERWISE UNUSABLE OR MISLEADING, NO GUARANTEES CAN BE MADE ABOUT THE AVAILABILITY OF THIS SERVICE, ESPECIALLY THE POLLING/SUBSCRIPTION MECHANISM
USE PURELY AT YOUR OWN RISK!
---

## Using the Bot
AviatorsBot is currently under development but will be available in Telegram 
under the name: `@AviatorsBot`

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
the crosswind and headwind for all runways of the airport. 

### Current Weather
The `/wx <ICAO>` command returns the current METAR and TAF and 
enhances the METAR with an icon for the flight category provided by 
the aviationweather.gov API. 

## Technical
### Technology Stack
AviatorsBot is written in Scala using akka for non-blocking IO. 

MongoDB is used as the backend. Accessed using the reactive-mongo Scala
library for extending the non-block and reactive concept also to this
part. 

### Production Ready, High Code Quality
The codebase has highly relevant unit and integration tests and a 
high coverage written using ScalaTests FunSuite.

To enable seamless testing a macro based DI library is used for 
Inversion of Control.  

Extensive logging and configurability add to the production readiness 
of the bot. 
