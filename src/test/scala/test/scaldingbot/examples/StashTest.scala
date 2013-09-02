package test.scaldingbot.examples

import scala.concurrent.Future
import org.scalatest.Matchers
import org.scalatest.WordSpecLike
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Stash
import akka.actor.actorRef2Scala
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import test.scaldingbot.examples.StashSpec.StashTest

object StashSpec {
  class StashTest extends Actor with Stash {
    implicit val ctx = context.dispatcher
    var on = false
    def receive = {
      case b : Boolean => {
        on = b
        unstashAll()
      }
      case _ => {
        if (on) sender ! true
        else {
          stash()
          val fut = Future("test")
          fut.onComplete {
            case x => {
              self ! true
            }
          }
        }
      }
    }
  }
}

class StashSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers {
  def this() = this(ActorSystem("TokenSpec"))

  "A stash test" must {

    "get a return message" in {
      val test = system.actorOf(Props[StashTest])
      test ! "foo"
      val msg = expectMsg(true)
    }
  }

}