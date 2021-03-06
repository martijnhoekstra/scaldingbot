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
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import akka.actor.Stash

object TokenRequester {
  def apply() = Props[TokenRequester]
}

case class TokenList(val tokens : Seq[Token]) extends AnyVal

class TokenRequester extends Actor  with Stash {
  import ExecutionContext.Implicits.global

  val req = TokenRequestAction(context.system)

  var cache: Map[String, Token] = Map.empty
  
    def receive = {
    case tl : TokenList => {
      this.cache = this.cache ++ tl.tokens.map(t => t.name -> t).toMap
      unstashAll()
    }
    case t: Token => {
      val cached = cache.get(t.name)
      cached match {
        case Some(token) if token.value == "" => stash()
        case Some(token) if token.value != t.value => sender ! token
        case _ => {
          stash()
          cache = cache + (t.name -> Token(t.name, ""))
          object ttype extends TokenType { val values = Set(t.name) }
          val ftok = req.perform(new ApiPropertySet(ttype :: Nil)).map(tr => tr.tokens)
          ftok.onSuccess {
            case seq: Seq[Token] => {
              self ! TokenList(seq)
            }
          }
        }
      }
    }
  }
}