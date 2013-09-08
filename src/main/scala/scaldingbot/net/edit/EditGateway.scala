package scaldingbot.net.edit

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.Actor
import akka.actor.ActorSelection
import akka.actor.ActorSelection.toScala
import akka.actor.Props
import akka.actor.Stash
import akka.actor.actorRef2Scala
import scaldingbot.net.ApiProperty
import scaldingbot.net.login.LoginCredentials
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginResponseBody
import scaldingbot.net.tokens.EditToken
import scaldingbot.settings.Settings
import spray.http.Uri.Authority
import scala.concurrent.ExecutionContext

object EditGateway {
  def apply(tokenRequester: ActorSelection, loginProvider: ActorSelection, authority: Authority = Settings.authority) = {
    Props(classOf[EditGateway], tokenRequester, loginProvider, authority)
  }
}

class EditGateway(tokenRequester: ActorSelection, loginProvider: ActorSelection, authority: Authority) extends Actor with Stash {
  var token: Option[EditToken] = None
  var loggedin = false
  implicit val sys = context.system
  val action = EditAction(authority)
  import ExecutionContext.Implicits.global
  import context._

  case object TokenInvalid

  case object mustbeloggedin extends ApiProperty {
    val name = "assert"
    val value = Some("user")
  }

  def receive = {
    case TokenInvalid => {
      this.token = None
    }
    case token: EditToken => {
      this.token = Some(token)
      unstashAll()
    }
    case logres: LoginResponseBody if logres.result == "Success" => {
      this.loggedin = true
      unstashAll()
    }
    case logfail: LoginResponseBody => {
      println("falure! " + logfail)
    }
    case edit: Edit => {
      val oldsender = sender
      if (!loggedin) {
        loginProvider ! LoginCredentials(Settings.user, Settings.pass)
        stash()
      } else {
        token match {
          case None => {
            stash()
            tokenRequester ! EditToken("-")
          }
          case Some(token) => {
            val futres = action.perform(edit.toApiPropertySet + makeeditoken(token) + mustbeloggedin)
            for (res <- futres) {
              res match {
                case success: EditSuccess => oldsender ! success
                case _ => {
                  println("I don't get why we got a " + res)
                  /* Need token stuff, doesn't exit yet
                  self ! TokenInvalid
                  tokenRequester ! token
                  stash()
                  * 
                  */
                }

              }
            }
          }
        }
      }
    }
  }

  def makeeditoken(token: EditToken) = {
    object mytok extends ApiProperty {
      val name = "token"
      val value = token.value
    }
    mytok

  }

}