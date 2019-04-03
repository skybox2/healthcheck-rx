import akka.actor.Actor

/**
  * Created by skylar on 3/28/19.
  */

abstract class HealthcheckItem(val key: String) {

  def generateHealthStatus(): HealthStatus

}


