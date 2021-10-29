package SimpleServers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.util.{Failure, Success}
import org.json._

object ServerDirectives extends App {
  implicit val system = ActorSystem("UsingDirectives")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val html:String= """ <html><body>Hello</body></html>""".stripMargin
  val jsonObject:JSONObject = new JSONObject();
  jsonObject.put("name", "deepak");
  jsonObject.put("address", "Bangalore");


  // equivalent directives for get, put, patch, delete, head, options
  //  curl -X GET localhost:8080/getjson

  val getpath=path("getjson") {
                  get {complete(HttpEntity(ContentTypes.`application/json`,jsonObject.toString) )} ~ //note tidle is used
                  post {complete(StatusCodes.Forbidden)}}

  val htmlpath=path("html") {complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,html) )}

//  curl -X GET localhost:8080/header -H "header:123"
  val header=path("header"){get{
                                  optionalHeaderValueByName("header") {
                                      case Some(s) => complete(HttpResponse(status = StatusCodes.OK, entity = s))
                                      case _ => complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,"No headers ") )}}
                                }

  val resource=path("resource" / "order" / IntNumber / IntNumber) {
            (id, inventory) =>println(s"I've got TWO numbers in my path: $id, $inventory")
                              complete(StatusCodes.OK)}

//  http://localhost:8080/query/item?id=45
  val parameters=    path("query" / "item") {
                        parameter('id.as[Int]) { (itemId: Int) =>println(s"I've extracted the ID as $itemId")
                                                                  complete(StatusCodes.OK)}}

  val error=path("notSupported") {
    failWith(new RuntimeException("Unsupported!")) // completes with HTTP 500
  }

  //extractRequest = payload
  val postpath= path("postjson") {
                      (post & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) =>
                                                                                                  val entity = request.entity
                                                                                                  val strictEntityFuture = entity.toStrict(2 seconds)
                                                                                                  val jsonFuture = strictEntityFuture.map(_.data.utf8String)
                                                                                                  onComplete(jsonFuture) {
                                                                                                    case Success(jsondata) =>
                                                                                                      val j:JSONObject=new JSONObject(jsondata)
                                                                                                      log.info(j.get("name").toString)
                                                                                                      complete(StatusCodes.OK)
                                                                                                    case Failure(ex) =>
                                                                                                      failWith(ex)
    }}}

val completeOkRoute = complete(StatusCodes.OK)
val allpath=path("index") { completeOkRoute  }
val dryRoute =  (path("about") | path("aboutUs")) { complete(StatusCodes.OK)  }


val chain =
          header~
          getpath~
          htmlpath~
          resource~
          parameters~
          error~
          postpath~
          allpath~
          dryRoute

  Http().bindAndHandle(chain, "localhost", 8080)
}
