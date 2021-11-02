package Client

//method,URI,headers,content,protocol (HTTP1.1/HTTP2.0) -- decompose
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpHeader, HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.stream.ActorMaterializer
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object VerySimpleClient extends App{
  implicit val system = ActorSystem("RequestLevelAPI")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher
  val responseFuture = Http().singleRequest(
    HttpRequest(uri = "https://reqres.in/api/users",
      method = HttpMethods.POST,
      entity = HttpEntity(ContentTypes.`application/json`,"""{"name":"morpheusx","job": "leader"}"""),
    ))

  responseFuture.onComplete {
    case Success(response) =>
      // VERY IMPORTANT
      response.discardEntityBytes()
      val strictEntityFuture = response.toStrict(2 seconds) //wait 2s
      println(s"The request was successful and returned: ${Unmarshaller.stringUnmarshaller(response.entity)}")
    case Failure(ex) =>
      println(s"The request failed with: $ex")
  }
}