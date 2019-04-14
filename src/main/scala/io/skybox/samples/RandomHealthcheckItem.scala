package io.skybox.samples

import io.skybox.HealthcheckItem
import io.skybox.domain._

import scala.util.Random

/**
  * Created by skylar on 4/14/19.
  */
class RandomHealthcheckItem extends HealthcheckItem(key = "random") {

  val r = Random

  override def generateHealthStatus(): HealthResponse = {
    val coinFlip = r.nextInt(2)
    coinFlip match {
      case 0 => HealthResponse(key, GREEN, "Yay we flipped heads")
      case 1 => HealthResponse(key, RED, "Oh no, we flipped tails")
    }

  }

}
