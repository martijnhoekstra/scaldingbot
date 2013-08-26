package scaldingbot.net.tokens

import spray.json.DefaultJsonProtocol

case class TokenResponse(tokens : TokenList) 
case class TokenList(edittoken : String)

object TokenResponseProtocol extends DefaultJsonProtocol {
  implicit val blah = jsonFormat1(TokenList)
  implicit val tokenResponseFormat = jsonFormat1(TokenResponse)
}
 