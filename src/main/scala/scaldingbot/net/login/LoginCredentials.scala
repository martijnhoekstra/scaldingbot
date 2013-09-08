package scaldingbot.net.login

import scaldingbot.net.ApiProperty
import scaldingbot.net.ApiPropertySet

case class LoginCredentials(user : String, pass : String){
  implicit def topset = {
    val loginname = LoginName(Some(user))
    val loginpass = LoginPassword(Some(pass))
    ApiPropertySet(Seq(loginname, loginpass).map(_.toApiPropertyValueSet))
  }
}

case class LoginName(value : Some[String]) extends ApiProperty {
  val name = "lgname"
}

case class LoginPassword(value : Some[String]) extends ApiProperty {
  val name = "lgpassword"
}

object LoginCredentials{
  implicit def topset(cred : LoginCredentials) = {
    val loginname = LoginName(Some(cred.user))
    val loginpass = LoginPassword(Some(cred.pass))
    ApiPropertySet(Seq(loginname, loginpass).map(_.toApiPropertyValueSet))
  }
}