package scaldingbot.net.tokens

import akka.actor.Actor
import akka.actor.Props
import spray.http.Uri.Authority
import spray.http.Uri.Host
import akka.actor.actorRef2Scala
import scala.concurrent.ExecutionContext.Implicits.global
import scaldingbot.net.Action
import scaldingbot.net.TokensActionType
import scala.concurrent.ExecutionContext
import scaldingbot.net.ApiPropertySet
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport
import scaldingbot.settings.Settings

object TokenRequester {
  def apply() = Props[TokenRequester]
}

class TokenRequester extends Actor {
  import ExecutionContext.Implicits.global
  
  val req = TokenRequestAction(context.system)

  def receive = {
    case t: Token => {
      val oldsender = sender
      object ttype extends TokenType { val values = Set(t.name) }
      val fresp = req.perform(new ApiPropertySet(ttype :: Nil))
      for (resp <- fresp ) {
        val editToken = resp.tokens.edittoken
        oldsender ! EditToken(editToken)
      }
    }
  }
}
