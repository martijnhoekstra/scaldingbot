package scaldingbot.net.tokens

import scaldingbot.net.ApiPropertyValueSet
import scaldingbot.net.ApiProperty

abstract sealed class Token(val name : String) extends ApiProperty{
  val value_ : String
  val value = Some(value_)
}

case class EditToken(val value_ : String) extends Token("edit")
case class BlockToken(val value_ : String) extends Token("block")
case class CentralAuthToken(val value_ : String) extends Token("centralauth")
case class DeleteToken(val value_ : String) extends Token("delete")
case class DeleteGlobalAccountToken(val value_ : String) extends Token("deleteglobalaccount")
case class EmailToken(val value_ : String) extends Token("email")
case class ImportToken(val value_ : String) extends Token("import")
case class MoveToken(val value_ : String) extends Token("move")
case class OptionsToken(val value_ : String) extends Token("options")
case class PatrolToken(val value_ : String) extends Token("patrol")
case class ProtectToken(val value_ : String) extends Token("protect")
case class SetGlobalAccountStatusToken(val value_ : String) extends Token("setglobalaccountstatus")
case class UnblockToken(val value_ : String) extends Token("unblock")
case class WatchToken(val value_ : String) extends Token("watch")
case class LoginToken(val value_ : String) extends Token("lgtoken")

trait TokenType extends ApiPropertyValueSet {
  val name = "type"
}

object Token {
  def apply(name : String, value_ : String) : Token = {
    name match {
      case "edit" => EditToken(value_)
      case "block" => BlockToken(value_)
      case "centralauth" => CentralAuthToken(value_)
      case "delete" => DeleteToken(value_)
      case "deleteglobalaccount" => DeleteGlobalAccountToken(value_)
      case "email" => EmailToken(value_)
      case "import" => ImportToken(value_)
      case "move" => MoveToken(value_)
      case "options" => OptionsToken(value_)
      case "patrol" => PatrolToken(value_)
      case "protect" => ProtectToken(value_)
      case "setglobalaccountstatus" => SetGlobalAccountStatusToken(value_)
      case "unblock" => UnblockToken(value_)
      case "watch" => WatchToken(value_)
      case "lgtoken" => LoginToken(value_)
    }
  }
}
