package scaldingbot.net.login

import scaldingbot.net.ApiProperty
import scaldingbot.net.ApiPropertySet

case class LoginCredentials(user : String, pass : String){
  implicit def topset = {
    ApiPropertySet(Seq(LoginName(user), LoginPassword(pass)).map(_.toApiPropertyValueSet))
  }
}

case class LoginName(value : String) extends ApiProperty {
  val name = "lgname"
}

case class LoginPassword(value : String) extends ApiProperty {
  val name = "lgpassword"
}