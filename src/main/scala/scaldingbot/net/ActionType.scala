package scaldingbot.net

abstract sealed class ActionType(val value : String) extends ApiProperty{
  val name = "action"
}


case object TokensActionType extends ActionType("tokens")
case object QueryActionType extends ActionType("query")
case object LoginActionType extends ActionType("login")
case object EditActionType extends ActionType("edit")