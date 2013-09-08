package scaldingbot.net.edit

import akka.actor.ActorSystem
import scaldingbot.net.Action
import scaldingbot.net.ActionType
import scaldingbot.net.ApiPropertySet
import scaldingbot.net.EditActionType
import spray.http.Uri.Authority
import spray.json.DefaultJsonProtocol._
import scaldingbot.settings.Settings


case class EditAction(val authority: Authority = Settings.authority)(implicit sys: ActorSystem) extends Action[EditResult] {
  val actiontype: ActionType = EditActionType
  val defaultproperties: ApiPropertySet = ApiPropertySet.empty
  import EditResultJsonProtocol._
  implicit val rootformat: spray.json.RootJsonFormat[scaldingbot.net.edit.EditResult] = EditResultJsonProtocol.EditResultFormat
  implicit val system: ActorSystem = sys
  
}