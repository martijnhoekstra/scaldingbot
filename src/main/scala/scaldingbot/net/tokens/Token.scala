package scaldingbot.net.tokens

import scaldingbot.net.query.ApiPropertyValueSet

abstract sealed class Token(val name : String){
  val value : String
}

case class EditToken(val value : String) extends Token("edit")
case class BlokToken(val value : String) extends Token("block")
case class CentralAuthToken(val value : String) extends Token("centralauth")
case class DeleteToken(val value : String) extends Token("delete")
case class DeleteGlobalAccontToken(val value : String) extends Token("deleteglobalaccount")
case class EmailToken(val value : String) extends Token("email")
case class ImportToken(val value : String) extends Token("import")
case class MoveToken(val value : String) extends Token("move")
case class OptionsToken(val value : String) extends Token("options")
case class PatrolToken(val value : String) extends Token("patrol")
case class ProtectToken(val value : String) extends Token("protect")
case class SetGlobalAccountStatusToken(val value : String) extends Token("setglobalaccountstatus")
case class UnblockToken(val value : String) extends Token("unblock")
case class WatchToken(val value : String) extends Token("watch")

trait TokenType extends ApiPropertyValueSet {
  val name = "type"
}
