package scaldingbot.net.login

import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat
import spray.json._
import DefaultJsonProtocol._

object LoginResponseJsonProtocol extends DefaultJsonProtocol {

  sealed abstract class LoginResponseBody {
    def result: String
    def cookieprefix: Option[String]
    def sessionid: Option[String]
  }

  case class LoginFailure(result: String, token: Option[String], cookieprefix: Option[String], sessionid: Option[String]) extends LoginResponseBody
  case class LoginSuccess(result: String, lgtoken: String, cookieprefix: Option[String], sessionid: Option[String], lguserid: Int, lgusername: String) extends LoginResponseBody
  case class LoginResponse(login: LoginResponseBody)

  implicit val LoginSuccessFormat = jsonFormat6(LoginSuccess)
  implicit val LoginFailureFormat = jsonFormat4(LoginFailure)

  implicit object LoginResponseBodyFormat extends RootJsonFormat[LoginResponseBody] {
    def write(b: LoginResponseBody) = b match {
      case ls: LoginSuccess => ls.toJson
      case lf: LoginFailure => lf.toJson
    }
    def read(value: JsValue) = value match {
      case obj: JsObject if (obj.fields.size == 6) => value.convertTo[LoginSuccess]
      case obj: JsObject => value.convertTo[LoginFailure]
    }
  }

}



