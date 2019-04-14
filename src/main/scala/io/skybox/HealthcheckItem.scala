package io.skybox

import domain._

/**
  * Created by skylar on 3/28/19.
  */

// TODO: Come up with a better name for this
// Maybe Healthchecker,
abstract class HealthcheckItem(val key: String) {

  def generateHealthStatus(): HealthResponse

}


