package scaldingbot.net.login

import scaldingbot.net.ApiPropertyValueSet
import scala.concurrent.ExecutionContext
import akka.actor.Actor
import scaldingbot.net.ApiPropertySet
import akka.actor.Props
import akka.actor.ActorSystem
import scala.util.Success
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginSuccess
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginFailure
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginResponseBody
import scaldingbot.net.tokens.LoginToken
import scala.concurrent.Future
import scala.util.Failure

object LoginProvider {
  def apply(implicit system: ActorSystem): Props = apply(new ApiPropertySet(Nil))(system)
  def apply(apiProperties: ApiPropertySet)(implicit system: ActorSystem) = Props(classOf[LoginProvider], new LoginAction(apiProperties))
  def apply(action : LoginAction)(implicit system : ActorSystem) = Props(classOf[LoginProvider],action)
}

class LoginProvider(action: LoginAction) extends Actor {
  def receive = {
    case cred: LoginCredentials => {
      import ExecutionContext.Implicits.global
      implicit val system = context.system
      val oldsender = sender
      val respf = action.perform(cred)

      val fin: Future[LoginResponseBody] = respf.flatMap(res => {
        res.login match {
          case success: LoginSuccess => {
            Future(success)
          }
          case notoken: LoginFailure if notoken.result == "NeedToken" => {
            val token = LoginToken(notoken.token.get)
            for (rez <- action.perform(cred.topset + token)) yield rez.login
          }
          case x => {
            Future(x)
          }
        }
      })
      
      fin.onComplete{
        case Success(success : LoginSuccess) => oldsender ! success
        case Success(other) => oldsender ! other
        case Failure(t) => println(t)
      }
    }
  }

}

