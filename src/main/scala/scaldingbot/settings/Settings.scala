package scaldingbot.settings


import spray.http.Uri.Authority
import spray.http.Uri.Host


object Settings {
  val user = "Martijn Hoekstra"
  lazy val pass : String = ???
  
  val authority = Authority(Host("test.wikipedia.org"))
  val email = "MartijnHoekstra@gmail.com"
    
  val source = "https://github.com/martijnhoekstra/scaldingbot/"
  val version = "0.2"
  val useragent = s"Scaldingbot/0.2 ($source; $email - User:$user) SprayClient/1.2-M8"
}
