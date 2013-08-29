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
import akka.actor.ActorSystem


case class TokenRequestAction(sys : ActorSystem) extends Action[TokenResponse] {
    import SprayJsonSupport._
    import DefaultJsonProtocol._
    import TokenResponseProtocol._

    implicit val rootformat = TokenResponseProtocol.tokenResponseFormat
    val actiontype = TokensActionType
    val defaultproperties = new ApiPropertySet(Nil)
    val authority = Settings.authority
    implicit val system = sys
  }
