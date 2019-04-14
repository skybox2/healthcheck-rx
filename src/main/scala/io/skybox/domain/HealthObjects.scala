package io.skybox.domain

/**
  * Created by skylar on 4/14/19.
  */

case class HealthResponse(name: String = "Health Status", status: HealthStatus, message: String)

sealed trait HealthStatus { def name: String }
case object GREEN extends HealthStatus { val name = "GREEN" }
case object YELLOW extends HealthStatus { val name = "YELLOW" }
case object RED extends HealthStatus { val name = "RED" }

case class HealthAsk()

