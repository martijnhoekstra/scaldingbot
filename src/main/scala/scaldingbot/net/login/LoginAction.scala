package scaldingbot.net.login

import scaldingbot.net.Action
import spray.json._
import scaldingbot.net.ActionType
import scaldingbot.net.LoginActionType
import scaldingbot.settings.Settings
import akka.actor.ActorSystem
import scaldingbot.net.query.ApiPropertyValueSet
import scaldingbot.net.query.ApiPropertySet
import spray.httpx.SprayJsonSupport
import spray.http.Uri.Authority

class LoginAction(loginProps: ApiPropertySet, val authority_ : Authority = Settings.authority)
                 (implicit val system : ActorSystem) extends Action[LoginResponse]{
  import SprayJsonSupport._
  import DefaultJsonProtocol._ 
  
  val actiontype = LoginActionType 
  val defaultproperties = loginProps
  val rootformat = jsonFormat6(LoginResponse)
  val authority = authority_
}
