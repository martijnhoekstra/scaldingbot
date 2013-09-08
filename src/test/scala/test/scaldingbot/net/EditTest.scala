package test.scaldingbot.net

import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.Seconds
import org.scalatest.time.Span
import akka.actor.ActorSystem
import scaldingbot.net.Action
import scaldingbot.net.TokensActionType
import spray.http.Uri.Authority
import spray.json.pimpString
import scaldingbot.net.ApiPropertySet
import spray.json._
import DefaultJsonProtocol._
import spray.http.FormData
import akka.testkit._
import org.scalatest.BeforeAndAfterAll
import scala.concurrent.ExecutionContext
import scaldingbot.net.login.LoginCredentials
import scaldingbot.settings.Settings
import scaldingbot.net.edit.EditAction
import scaldingbot.net.tokens.TokenRequester
import scaldingbot.net.edit.EditGateway
import akka.actor.ActorSelection
import scaldingbot.net.edit.Edit
import scaldingbot.net.edit.Content
import scala.util.Random
import org.scalatest.time.Span
import org.scalatest.time.Seconds
import scaldingbot.net.edit.EditResult
import scala.concurrent.duration._
import scaldingbot.net.login.LoginProvider
import scaldingbot.net.login.LoginAction

class EditSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with ScalaFutures {
  implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds), interval = Span(1, Seconds))
  def this() = this(ActorSystem("EditSpec"))
  import ExecutionContext.Implicits.global

  val credentials = LoginCredentials(Settings.user, Settings.pass)

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An edit gateway " must {
    "edit a testpage on test wiki" in {
      val tokenselect = system.actorSelection(system.actorOf(TokenRequester()).path)
      val loginaction = new LoginAction(ApiPropertySet())
      val loginselection = system.actorSelection(system.actorOf(LoginProvider(loginaction)).path)
      val gateway = system.actorOf(EditGateway(tokenselect, loginselection))
      val content = Content(None, "test content" + new Random().nextInt /*new Random().nextString(10) */, "test edit")
      val edit = Edit(Left("Scaldingbot"), content, None )
      gateway ! edit
      val result = expectMsgPF(10 seconds) {
        case editresult : EditResult => editresult
      }
      result.result should be("Success")
    }
  }

}
