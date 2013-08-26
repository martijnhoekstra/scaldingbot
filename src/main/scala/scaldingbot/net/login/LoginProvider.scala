package scaldingbot.net.login

import scaldingbot.net.query.ApiPropertyValueSet
import scala.concurrent.ExecutionContext
import akka.actor.Actor
import scaldingbot.net.query.ApiPropertySet

class LoginProvider extends Actor {
  def receive = {
    case properties: ApiPropertySet => {
      import ExecutionContext.Implicits.global
      implicit val system = context.system
      //TODO: Keep login action alive?
      val login = new LoginAction(properties)
      login.perform(properties)
    }
  }

}

