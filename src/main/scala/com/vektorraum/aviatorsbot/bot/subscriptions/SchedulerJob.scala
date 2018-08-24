package com.vektorraum.aviatorsbot.bot.subscriptions

import com.typesafe.scalalogging.Logger
import nl.grons.metrics4.scala.DefaultInstrumented
import org.quartz.{DisallowConcurrentExecution, Job, JobExecutionContext, JobExecutionException}

import scala.util.control.NonFatal

@DisallowConcurrentExecution
class SchedulerJob extends Job with DefaultInstrumented {
  private val logger = Logger(getClass)
  private val meter = metrics.meter("subscription-handler-job-exec")

  override def execute(context: JobExecutionContext): Unit = {
    try {
      logger.info("Executing SubscriptionHandlerJob")
      meter.mark()

      SchedulerJob.executor()
    } catch {
      case NonFatal(ex) =>
        logger.warn("Exception thrown during SchedulerJob execution", ex)
        throw new JobExecutionException(ex)
    }
  }
}

object SchedulerJob {
  var executor: () => Unit = { throw new RuntimeException("Executor in SchedulerJob has not been set!") }
}
