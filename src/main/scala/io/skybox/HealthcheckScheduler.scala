package io.skybox

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem}

import scala.concurrent.duration.FiniteDuration

/**
  * Created by skylar on 4/14/19.
  */
class HealthcheckScheduler(healthchecks: List[HealthcheckItem],
                           healthcheckActorRef: ActorRef,
                           initialDelay: Int,
                           intervalSeconds: Int
                           )(implicit system: ActorSystem) {

  val scheduler = system.scheduler

  val head = healthchecks.head // TODO: Change this to a for each for each list item
  implicit val ec = system.dispatcher

  val initialDuration = FiniteDuration(initialDelay, TimeUnit.SECONDS)
  val intervalDuration = FiniteDuration(intervalSeconds, TimeUnit.SECONDS)

  println(s"We're creating the health check scheduler")

  val cancellable = scheduler.schedule(initialDuration, intervalDuration) {
    () =>
      println(s"We've run the scheduled thing")
      healthcheckActorRef ! head.generateHealthStatus
  }
}

object HealthcheckScheduler {
  def apply(healthchecks: List[HealthcheckItem],
            healthcheckActorRef: ActorRef,
            initialDelay: Int = 0,
            intervalSeconds: Int = 60
           )(implicit system: ActorSystem) = {
    new HealthcheckScheduler(healthchecks, healthcheckActorRef, initialDelay, intervalSeconds)

  }
}
