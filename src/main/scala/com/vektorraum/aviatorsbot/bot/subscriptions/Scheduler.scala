package com.vektorraum.aviatorsbot.bot.subscriptions

import java.time.ZonedDateTime
import java.util.Date

import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import com.vektorraum.aviatorsbot.bot.util.TimeUtil.implicits._
import org.quartz.JobBuilder._
import org.quartz.SimpleScheduleBuilder._
import org.quartz.TriggerBuilder._
import org.quartz.impl.StdSchedulerFactory

import scala.concurrent.duration.Duration


object Scheduler {
  private val logger = Logger(getClass)

  val scheduler: org.quartz.Scheduler = StdSchedulerFactory.getDefaultScheduler

  /**
    * Schedules a function for periodic execution as defined in the config.
    *
    * Must only be called once!
    *
    * Example configuration:
    * interval = 5 minutes
    * initial_delay = 2 minutes
    *
    * Any string parsable by Scalas Duration is permissible.
    *
    * The job will run repeated until the program exists every interval time and after the
    * initial_delay has passed.
    *
    * @param config Configuration as shown above
    * @param func Function to be run by the scheduler
    */
  def apply(config: Config, func: () => Unit): Unit = {
    SchedulerJob.setExecutor(func)

    val interval = Duration(config.getString("interval"))
    val inital_delay = Duration(config.getString("initial_delay"))

    val startDate = Date.from(
      ZonedDateTime
        .now()
        .plus(inital_delay)
        .toInstant
    )

    val job = newJob(classOf[SchedulerJob])
      .withIdentity("schedulerJob")
      .build()

    val trigger = newTrigger()
      .withIdentity("schedulerJobTrigger")
      .withSchedule(simpleSchedule()
        .withIntervalInMilliseconds(interval.toMillis)
        .repeatForever())
      .startAt(startDate)
      .build()

    scheduler.start()

    if (scheduler.checkExists(job.getKey)) {
      scheduler.deleteJob(job.getKey)
      logger.warn("Replacing old job in Scheduler! Only to be used for testing.")
    }
    scheduler.scheduleJob(job, trigger)
  }

}
