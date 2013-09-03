package scaldingbot.net.login

import scaldingbot.net.Action
import spray.json._
import scaldingbot.net.ActionType
import scaldingbot.net.LoginActionType
import scaldingbot.settings.Settings
import akka.actor.ActorSystem
import scaldingbot.net.ApiPropertyValueSet
import scaldingbot.net.ApiPropertySet
import spray.httpx.SprayJsonSupport
import spray.http.Uri.Authority
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginResponse

class LoginAction(loginProps: ApiPropertySet, val authority : Authority = Settings.authority)
                 (implicit val system : ActorSystem) extends Action[LoginResponse]{
  import SprayJsonSupport._
    
  val actiontype = LoginActionType 
  val defaultproperties = loginProps
  import LoginResponseJsonProtocol._
  val rootformat = jsonFormat1(LoginResponseJsonProtocol.LoginResponse)
}
