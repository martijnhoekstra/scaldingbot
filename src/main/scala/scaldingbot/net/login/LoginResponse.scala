package scaldingbot.net.login

case class LoginResponse(result : String, lgtoken: String, cookieprefix : String, sessionid: String, lguserid : Option[Int], lgusername : Option[String])
