import scala.concurrent.ExecutionContext

/**
  * Created by skylar on 3/28/19.
  */
object HealthcheckRxApp extends App {

  implicit val ec: ExecutionContext = ExecutionContext.global
  val healthcheckRx = HealthcheckRx(List.empty)

  healthcheckRx.returnHealthObject()
}
