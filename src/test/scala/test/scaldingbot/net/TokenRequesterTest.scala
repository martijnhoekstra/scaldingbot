package test.scaldingbot.net

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
import scaldingbot.net.tokens.EditToken
import scaldingbot.net.tokens.TokenRequestAction
import scaldingbot.net.tokens.TokenRequester
import scaldingbot.net.tokens.TokenType

class TokenSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with ScalaFutures {
  implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds), interval = Span(1, Seconds))
  def this() = this(ActorSystem("TokenSpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
    TestKit.shutdownActorSystem(_system)
  }

  "A token requester" must {

    "get valid edit tokens from English Wikipedia" in {
      val requester = system.actorOf(TokenRequester())
      requester ! EditToken("-")
      val tokenstring = expectMsgPF(10 seconds) {
        case e: EditToken => e.value
      }
      tokenstring match {
        case Some(str) => str should endWith("+\\")
        case x => x should be(null)
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
            res.tokens.head.value match {
              case Some(str) => str should endWith("+\\")
              case x => x should be(null)
            } 
          }
      }
    }
  }

}