package test.scaldingbot.net

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.Seconds
import org.scalatest.time.Span
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import scaldingbot.net.ApiPropertySet
import scaldingbot.net.login.LoginAction
import scaldingbot.net.login.LoginCredentials
import scaldingbot.net.login.LoginProvider
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginFailure
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginResponseBody
import scaldingbot.net.login.LoginResponseJsonProtocol.LoginSuccess
import scaldingbot.net.tokens.LoginToken
import scaldingbot.settings.Settings
import scala.concurrent.ExecutionContext

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
          case fin => println(fin); fin.result should be("Success")
        }
      }
    }
  }
  
  
  
  "A login provider" must {
    "simply log in" in {
      val provider = system.actorOf(LoginProvider(system))
      provider ! credentials
      val result = expectMsgPF(10 seconds){
        case loginSuccess : LoginSuccess => loginSuccess
        case x : LoginResponseBody => x
      }
      result.result should be("Success")
     }
  }

}