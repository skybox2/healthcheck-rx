import scala.concurrent.ExecutionContext

import akka.actor.Actor

import scala.collection.mutable.Map

// TODO: Define how we want to access the actor and update its state
class HealthcheckRx(healthchecks: List[HealthcheckItem]) {


  // Define the initial healthcheck state
  val initialHealthState = healthchecks map (hc => HealthResponse(hc.key, YELLOW, "Initializing health"))

  // Instantiate the actor -- how to initialize the state (start off yellow, initializing)
  // TODO: Figure out how to get an actor ref
  val healthcheckActor = new HealthcheckActor

  // TODO: Figure out how to send an ask to the actor
  def returnHealthObject(): [List[HealthResponse]] = {
    healthcheckActor.
  }

}


object HealthcheckRx {
  // Pass in the execution context so that we can create health check actors and
  def apply(healthchecks: List[HealthcheckItem])(implicit executionContext: ExecutionContext): HealthcheckRx = {
    new HealthcheckRx(healthchecks)
  }
}

class HealthcheckActor extends Actor {
  private var healthStatusState: Map[String, HealthResponse] = Map.empty[String, HealthResponse]
  override def receive: Receive = {
    case healthStatus: HealthResponse =>
      healthStatusState update (healthStatus.name, healthStatus)
    case healthAsk: HealthAsk =>
      // Send back the health status state to the asker
      sender() ! healthStatusState

    case _ => println(s"You didn't send a health response to the health actor")
  }
}

case class HealthResponse(name: String = "Health Status", status: HealthStatus, message: String)

sealed trait HealthStatus { def name: String }
case object GREEN extends HealthStatus { val name = "GREEN" }
case object YELLOW extends HealthStatus { val name = "YELLOW" }
case object RED extends HealthStatus { val name = "RED" }

case class HealthAsk()
