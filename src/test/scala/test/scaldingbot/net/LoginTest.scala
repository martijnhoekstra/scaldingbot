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
import scaldingbot.net.query.ApiPropertySet
import scaldingbot.net.tokens.TokenType
import org.scalatest.concurrent.Futures
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.Span
import org.scalatest.time.Seconds



class LoginSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll with ScalaFutures {
  implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds), interval = Span(1, Seconds))
  def this() = this(ActorSystem("LoginSpec"))
  
  
  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
  
  "The login system" must {
    "be able to log in users through the API" in {
      ???
      //val requester = system.actorOf(LoginProvider())
      //requester ! LoginData("")
      //expectMsgPF(10 seconds){
      //  case r : LoginResponse => {
      //    r.result should be("success")
      //  }
      //  case x => {
      //    true should be(false)
      //  }
      //}
    }

  }
  
}