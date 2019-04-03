import scala.concurrent.ExecutionContext

/**
  * Created by skylar on 3/28/19.
  */
object HealthcheckRxApp extends App {

  implicit val ec: ExecutionContext = ExecutionContext.global

  // TODO: Define some preliminary tests that we need to
  val healthcheckRx = HealthcheckRx(List.empty)

}
