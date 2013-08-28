package scaldingbot.net.login

import scaldingbot.net.query.ApiPropertyValueSet
import scala.concurrent.ExecutionContext
import akka.actor.Actor
import scaldingbot.net.query.ApiPropertySet
import akka.actor.Props
import akka.actor.ActorSystem

object LoginProvider{
  def apply(implicit system : ActorSystem) : Props = apply(new ApiPropertySet(Nil))(system)
  def apply(apiProperties : ApiPropertySet)(implicit system : ActorSystem) = Props(classOf[LoginProvider], apiProperties)
}

class LoginProvider(action : LoginAction) extends Actor {
  def receive = {
    case cred : LoginCredentials => {
      import ExecutionContext.Implicits.global
      implicit val system = context.system
      //TODO: Keep login action alive?
      //val login = new LoginAction(properties)
      //login.perform(properties)
    }
  }

}

