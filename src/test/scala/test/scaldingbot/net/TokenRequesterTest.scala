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

class TokenSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with ScalaFutures {
  implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds), interval = Span(1, Seconds))
  def this() = this(ActorSystem("TokenSpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A token requester" must {

    "get valid edit tokens from English Wikipedia" in {
      val requester = system.actorOf(TokenRequester())
      requester ! EditToken("-")
      expectMsgPF(30 seconds) {
        case e: EditToken => {
          e.value should endWith("+\\")
        }
        case x => {
          true should be(false)
        }
      }
    }

  }

  "A token request action" should {
    "get valid edit tokens" in {
      val reqaction = TokenRequestAction(system)
      object ttype extends TokenType { val values = Set("edit") }
      val resp = reqaction.perform(new ApiPropertySet(ttype :: Nil))
      whenReady(resp) {
        res =>
          {
            res.tokens.length should be > (0)
            res.tokens.head.value should endWith("+\\")
          }
      }
    }
  }

}