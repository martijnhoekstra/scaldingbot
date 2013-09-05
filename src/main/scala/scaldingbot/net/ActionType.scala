package scaldingbot.net

abstract sealed class ActionType(val value_ : String) extends ApiProperty{
  val value = Some(value_)
  val name = "action"
}


case object TokensActionType extends ActionType("tokens")
case object QueryActionType extends ActionType("query")
case object LoginActionType extends ActionType("login")
case object EditActionType extends ActionType("edit")