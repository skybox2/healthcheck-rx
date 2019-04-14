package io.skybox

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext

/**
  * Created by skylar on 4/14/19.
  */
object HealthcheckRxApp extends App {

  implicit val ec: ExecutionContext = ExecutionContext.global
  implicit val system: ActorSystem = ActorSystem()
  val healthcheckRx = HealthcheckRx(List.empty)


  val response = healthcheckRx.getSystemHealthStatus
  println(response)
}
