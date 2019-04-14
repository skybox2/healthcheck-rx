package io.skybox

import akka.actor.ActorSystem
import io.skybox.samples.RandomHealthcheckItem

import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}

/**
  * Created by skylar on 4/14/19.
  */
object HealthcheckRxApp extends App {

  implicit val ec: ExecutionContext = ExecutionContext.global
  implicit val system: ActorSystem = ActorSystem()
  val healthcheckRx = HealthcheckRx(List(new RandomHealthcheckItem))

  healthcheckRx.getSystemHealthStatus onComplete {
    case Success(hs) => println(hs.toString)
    case Failure(t) => println(s"Error returned ${t.getMessage}")
  }

}
