package scaldingbot.net.tokens

import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat
import spray.json.JsObject
import spray.json.JsValue
import spray.json.JsString
import spray.json.DeserializationException

case class TokenResponse(tokens: Seq[Token])

object TokenResponseProtocol extends DefaultJsonProtocol {
  implicit object tokenListFormat extends RootJsonFormat[Seq[Token]] {
    def write(tokens: Seq[Token]) = {
      val pairs = tokens.map(t => (t.name + "token" -> t.value)).toList.toMap
      //JsObject(pairs)
      ???
    }
    def read(value: JsValue) = {
      val fields = value.asJsObject.fields
      fields.foldLeft(Nil: List[Token])((r, f) => {
        val tok = f match {
          case (s: String, JsString(v)) if s.endsWith("token") => Token(s.substring(0, s.length() -5), v)
          case (s: String, JsString(v)) => Token(s, v)
          case _ => throw new DeserializationException("string, string expected")
        }
        tok :: r
      })
    }
  }
  implicit val tokenResponseFormat = jsonFormat1(TokenResponse)
}
 