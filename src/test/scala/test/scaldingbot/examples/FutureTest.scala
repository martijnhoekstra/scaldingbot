package test.scaldingbot.net

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalatest.WordSpec
import org.scalatest.concurrent._
import org.scalatest.concurrent.Futures
import org.scalatest.WordSpecLike
import org.scalatest.Matchers

class FutureSpec extends WordSpecLike with ScalaFutures with Matchers {
  "A future" should {
    "be able to passed to whenReady" in {
      1 should be(1)
      val fut = future { 42 }
      whenReady(fut) { res => res should be(42) }
    }
  }
}