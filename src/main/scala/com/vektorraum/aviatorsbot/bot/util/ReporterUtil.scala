package com.vektorraum.aviatorsbot.bot.util

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import com.codahale.metrics.{MetricFilter, MetricRegistry}
import com.codahale.metrics.graphite.{Graphite, GraphiteReporter, PickledGraphite}
import com.typesafe.config.Config

object ReporterUtil {
  /**
    * Builds a dropwizard graphite reporter based upon the configuration
    *
    * Correct configuration example:
    *
    * enabled = true
    * host = "localhost"
    * port = 2003
    * prefix = "aviatorsbot"
    *
    * @param config Configuration at most detailed level
    * @param registry Metrics registry
    * @return Configured reporter ready to be started
    */
  def graphiteReporter(config: Config, registry: MetricRegistry): Option[GraphiteReporter] = {
    if(config.getBoolean("enabled")) {
      val graphite = new Graphite(new InetSocketAddress(config.getString("host"), config.getInt("port")))

      val reporter = GraphiteReporter
        .forRegistry(registry)
        .prefixedWith(config.getString("prefix"))
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .filter(MetricFilter.ALL)
        .build(graphite)

      Some(reporter)
    } else {
      None
    }
  }

}
