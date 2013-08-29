package test.scaldingbot.net

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.TestKit
import org.scalatest.WordSpecLike
import org.scalatest.matchers.MustMatchers
import org.scalatest.BeforeAndAfterAll
import akka.testkit.ImplicitSender
import scaldingbot.net.tokens.TokenRequester
import scaldingbot.net._
import scala.concurrent.duration._
import org.scalatest.Matchers
import scaldingbot.net.tokens.EditToken
import scaldingbot.net.tokens.TokenRequestAction
import scaldingbot.net.ApiPropertySet
import scaldingbot.net.tokens.TokenType
import org.scalatest.concurrent.Futures
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.Span
import org.scalatest.time.Seconds
import scaldingbot.net.login.LoginAction
import scaldingbot.net.login.LoginCredentials
import scala.concurrent.ExecutionContext
import spray.json.DeserializationException
import spray.httpx.PipelineException
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginFailure
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginResponse
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginSuccess
import scaldingbot.net.tokens.LoginToken
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginResponseBody
import scala.concurrent.Future
import scaldingbot.settings.Settings

class LoginSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with ScalaFutures {
  implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds), interval = Span(1, Seconds))
  def this() = this(ActorSystem("LoginSpec"))
  import ExecutionContext.Implicits.global
  val credentials = LoginCredentials(Settings.user, Settings.pass)

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A login action " must {
    "log in" in {
      if (credentials.pass != "") {

        val login = new LoginAction(ApiPropertySet())
        val respf = login.perform(credentials.topset)

        val fin: Future[LoginResponseBody] = respf.flatMap(res => {
          res.login match {
            case success: LoginSuccess => {
              Future(success)
            }
            case fail: LoginFailure if fail.result == "NeedToken" => {
              val token = LoginToken(fail.token.get)
              for (rez <- login.perform(credentials.topset + token)) yield rez.login
            }
            case other => {
              other should be(LoginSuccess)
              Future { other }
            }
          }
        })

        whenReady(fin) {
          case fin => fin.result should be("Success")
        }

      }
    }
  }

}