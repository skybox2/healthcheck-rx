import scala.concurrent.ExecutionContext
import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.duration._
import akka.util.Timeout
import akka.pattern.ask

import scala.util.{Failure, Success}

//import scala.collection.mutable.Map

class HealthcheckRx(healthchecks: List[HealthcheckItem], requestTimeout: Int = 2)(implicit ec: ExecutionContext) {

  private val system = ActorSystem()
  private implicit val timeout = Timeout(requestTimeout.seconds)
  private val initialHealthState = (healthchecks map
    (hc => (hc.key, HealthResponse(hc.key, YELLOW, "Initializing health")))).toMap

  // Instantiate the actor -- how to initialize the state (start off yellow, initializing)
  private val healthcheckActorRef = system.actorOf(Props(new HealthcheckActor(initialHealthState)))

  // TODO: Figure out how to send an ask to the actor
  def returnHealthObject(): Unit = {
    healthcheckActorRef ? HealthAsk onComplete {
      case Success(healthList) =>
        println(s"we got a success: $healthList")
      case Failure(ex) =>
        println(s"${ex.getMessage}")
    }
  }

}


object HealthcheckRx {
  // Pass in the execution context so that we can create health check actors and
  def apply(healthchecks: List[HealthcheckItem])(implicit executionContext: ExecutionContext): HealthcheckRx = {
    new HealthcheckRx(healthchecks)
  }
}

class HealthcheckActor(initialHealthState: Map[String, HealthResponse]) extends Actor {

  // Create the mutable state inside of the actor
  private var healthStatusState: Map[String, HealthResponse] = initialHealthState

  override def receive: Receive = {
    case HealthResponse =>
      //healthStatusState update (healthStatus.name, healthStatus)
    case HealthAsk =>
      // Send back the health status state to the asker
      sender() ! healthStatusState
    case _ =>
      println(s"HealthAsk and HealthResponse only to the health actor")
  }
}

case class HealthResponse(name: String = "Health Status", status: HealthStatus, message: String)

sealed trait HealthStatus { def name: String }
case object GREEN extends HealthStatus { val name = "GREEN" }
case object YELLOW extends HealthStatus { val name = "YELLOW" }
case object RED extends HealthStatus { val name = "RED" }

case class HealthAsk()
