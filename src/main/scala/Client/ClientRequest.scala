package Client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import scala.util.{Failure, Success}

object ClientRequest extends App {

  implicit val system = ActorSystem("RequestLevelAPI")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val responseFuture = Http().singleRequest(HttpRequest(uri = "http://www.google.com"))

  responseFuture.onComplete {
    case Success(response) =>
      // VERY IMPORTANT
      response.discardEntityBytes()
      println(s"The request was successful and returned: $response")
    case Failure(ex) =>
      println(s"The request failed with: $ex")
  }

}