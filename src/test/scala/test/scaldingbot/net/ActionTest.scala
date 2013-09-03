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
import spray.http.Uri.Host
import spray.http.Uri.Path
import spray.http.Uri.{ Query => SQuery }
import spray.json.pimpString
import scaldingbot.net.ApiPropertySet
import spray.json._
import DefaultJsonProtocol._
import spray.http.FormData
import akka.testkit._
import org.scalatest.BeforeAndAfterAll

object TestAction extends Action[Boolean] {
  val actiontype = TokensActionType
  val authority = Authority(Host("test.wikipedia.org"))
  val defaultproperties = new ApiPropertySet(Nil)
  val system = ActorSystem("ActionSpec")
  object TestFormat extends RootJsonFormat[Boolean] {
    def write(b: Boolean) = JsObject()
    def read(js: JsValue) = true
  }
  val rootformat = TestFormat

}

class ActionSpec(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with ScalaFutures with Matchers with BeforeAndAfterAll {
  implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds), interval = Span(1, Seconds))

    override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
  
  
  "An action" must {

    "be able to create a valid request object from on the Test.Wikipedia's API" in {
      val data = FormData(Map("action" -> "query", "list" -> "exturlusage", "euquery" -> "slashdot.org"))

      val get = TestAction.createRequest(Some(data))
      get.uri.path should be(Path("/w/api.php"))
      get.uri.query should be(Nil)
      get.headers should be(Nil)
    }

    "be able to perform an action against the English Wikipedia API" in {

      val fres = TestAction.perform(new ApiPropertySet(TokensActionType.toApiPropertyValueSet :: Nil))
      whenReady(fres) {
        res => res should be(true)
      }

    }

  }

}