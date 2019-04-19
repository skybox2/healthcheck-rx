package io.skybox

import scala.concurrent.{ExecutionContext, Future}
import akka.actor.{Actor, ActorSystem, PoisonPill, Props}

import scala.concurrent.duration._
import akka.util.Timeout
import akka.pattern.ask
import domain._

// Should re-use the actor system from your normal application becauase it's not lightweight
class HealthcheckRx(healthchecks: List[HealthcheckItem], requestTimeout: Int = 2, timerMillis: Int = 60)
                   (implicit ec: ExecutionContext, system: ActorSystem) {

  // Improve the readability of the API
  type SystemHealthStatus = Map[String @unchecked, HealthResponse @unchecked]

  private implicit val timeout = Timeout(requestTimeout.seconds)

  // The initial health state is to be defined as YELLOW
  private val initialHealthState = (healthchecks map
    (hc => (hc.key, HealthResponse(hc.key, YELLOW, "Initializing health")))).toMap

  println(s"Initial health state for the actor $initialHealthState")

  // Instantiate the actor -- how to initialize the state (start off yellow, initializing)
  private val healthcheckActorRef = system.actorOf(Props(new HealthcheckActor(initialHealthState)))

  // TODO: Instantiate a scheduler here, and pass it the actor ref
  private val scheduler = HealthcheckScheduler(healthchecks, healthcheckActorRef)

  def getSystemHealthStatus: Future[SystemHealthStatus] = {
    (healthcheckActorRef ? HealthAsk).map {
      case m: SystemHealthStatus => m
      case _ => Map.empty[String, HealthResponse] // Figure out how to handle this (it covers errors right now)
    }
  }

  // Shutdown the healthcheck actor and then shutdown the scheduler
  def shutdownHealthSystem: Future[Boolean] = {
    Future(healthcheckActorRef ! PoisonPill) flatMap
      ( _ => scheduler.shutdownScheduler )
  }
}

// Companion object for HealthcheckRx class, which will be a wrapper for the actor that we use
object HealthcheckRx {
  // Pass in the execution context so that we can create health check actors and
  def apply(healthchecks: List[HealthcheckItem])(implicit executionContext: ExecutionContext, system: ActorSystem): HealthcheckRx = {
    new HealthcheckRx(healthchecks)
  }
}

// The actor should only be used wrapped inside a healthcheck rx object, so we don't have to
// directly interact with the actor ref at all in our server code
class HealthcheckActor(initialHealthState: Map[String, HealthResponse]) extends Actor {

  // Create the mutable state inside of the actor
  private var healthStatusStates: Map[String, HealthResponse] = initialHealthState

  // Thread safe so no worries about contention over the health status map
  override def receive: Receive = {
    case hr: HealthResponse =>
      // TODO: Log any health state changes here
      println(s"Received a new health check update: ${hr}")
      healthStatusStates = healthStatusStates + ((hr.name, hr)) // Update the map based on the key
    case HealthAsk =>
      // Send back the health status state to the asker
      sender() ! healthStatusStates
    case _ =>
      println(s"HealthAsk and HealthResponse only to the health actor")
  }
}

