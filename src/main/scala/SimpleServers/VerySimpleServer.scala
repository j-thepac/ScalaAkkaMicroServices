package SimpleServers


//curl -X POST http://localhost:8080/home?id=5 -H "header:123" -d '{"name":"deepak"}'
// method,URI,headers,content,protocol (HTTP1.1/HTTP2.0) -- decompose
//https://blog.rockthejvm.com/akka-http-json/
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{Unmarshaller}

object VerySimpleServer extends App {
  implicit val system = ActorSystem("LowLevelServerAPI")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher
  val syncRequestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(HttpMethods.POST, uri@Uri.Path("/home"), header, entity, _) =>
    {
      val query = uri.query()
      val id: Option[Int] = query.get("id").map(_.toInt)
      print(header,Unmarshaller.stringUnmarshaller(entity),id)
      HttpResponse(StatusCodes.Found,entity = HttpEntity(ContentTypes.`application/json`,"""{"name":"deepak"}"""))
    }}
  Http().bindAndHandleSync(syncRequestHandler, "localhost", 8080)
}