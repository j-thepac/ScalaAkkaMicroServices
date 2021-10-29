package TokenService
//https://github.com/reallylabs/jwt-scala


import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.StandardRoute
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtSprayJson}

import scala.concurrent.duration._
import org.json._

import scala.util.{Failure, Success}

case class LoginRequest(username: String, password: String)

object TokenServerReceiver extends App{

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val superSecretPasswordDb = Map(
    "admin" -> "admin"
  )

  val algorithm = JwtAlgorithm.HS256
  val secretKey = "secret"

  def checkPassword(username: String, password: String): Boolean =
    superSecretPasswordDb.contains(username) && superSecretPasswordDb(username) == password

  def createToken(username: String, expirationPeriodInDays: Int): String = {
    val claims = JwtClaim(
      expiration = Some(System.currentTimeMillis() / 1000 + TimeUnit.DAYS.toSeconds(expirationPeriodInDays)),
      issuedAt = Some(System.currentTimeMillis() / 1000),
      issuer = Some("org")
    )
    Jwt.encode(claims, secretKey, algorithm) // JWT string
  }

  def isTokenExpired(token: String): Boolean = Jwt.decode(token, secretKey, Seq(algorithm)) match {
    case Success(claims) => claims.expiration.getOrElse(0L) < System.currentTimeMillis() / 1000
    case Failure(_) => true
  }

  def isTokenValid(token: String): Boolean = Jwt.isValid(token, secretKey, Seq(algorithm))

//  curl -X POST localhost:8080/auth -d "{\"name\":\"deepak\" , \"password\":\"pass\"}"
  val loginRoute = {(path("auth") & post & pathEndOrSingleSlash & extractRequest & extractLog) {
          (request, log) =>
                        val entity = request.entity
                        val strictEntityFuture = entity.toStrict(2 seconds)
                        val jsonFuture = strictEntityFuture.map(_.data.utf8String)

                        onComplete(jsonFuture) {
                          case Success(jsondata) => {
                                                        val j: JSONObject = new JSONObject(jsondata)
                                                        val usn= j.get("name").toString
                                                        val pass= j.get("password").toString
                                                        if (checkPassword(usn, pass)) {
                                                          log.info(createToken(j.get("name").toString, 1))
                                                          complete(StatusCodes.OK)}
                                                          complete(HttpResponse(status = StatusCodes.Unauthorized,entity = "Usn, password did not match")) }
                          case Failure(ex) =>  failWith(ex)
                        }
                      }}
//
//  a="<token here>"
//  curl -X GET localhost:8080/secureEndpoint -H "Authorization:$a"
  val authenticatedRoute =
    (path("secureEndpoint") & get) {
      optionalHeaderValueByName("Authorization") {
                case Some(token) =>
                          if (isTokenValid(token)) {
                            if (isTokenExpired(token))
                              complete(HttpResponse(status = StatusCodes.Unauthorized, entity = "Token expired."))
                             else
                              complete("SUccess!")
                          }
                          else
                            complete(HttpResponse(status = StatusCodes.Unauthorized, entity = "Token is invalid, or has been tampered with."))
                 case _ => complete(HttpResponse(status = StatusCodes.Unauthorized, entity = "No token provided!"))
      }
    }

  val route = loginRoute ~ authenticatedRoute
  Http().bindAndHandle(route, "localhost", 8080)
}
