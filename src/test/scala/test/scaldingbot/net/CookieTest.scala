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
import scaldingbot.net.query.ApiPropertySet
import spray.json._
import DefaultJsonProtocol._
import scaldingbot.net.CookieJar
import scaldingbot.net.SBHttpCookie
import org.joda.time.{DateTime => JT}

class CookieSpec extends WordSpecLike with ScalaFutures with Matchers {
  val jar = CookieJar("", Map.empty, Set.empty)
  "An cookie jar" must {
    
    "retrieve a cookie set for the same domain" in {
      val cookie = SBHttpCookie("myname", "myvalue", None)
      val newjar = jar.setCookie(cookie, "www.example.org")
      val fetched = newjar.cookiesfor("www.example.org")
      fetched.count(c => true) should be(1)
      fetched.head.value should be("myvalue")
      fetched.head.name should be("myname")
    }
    
    "retrieve a cookie set for a parent domain" in {
      val cookie = SBHttpCookie("myname", "myvalue", None)
      val newjar = jar.setCookie(cookie, "example.org")
      val fetched = newjar.cookiesfor("www.example.org")
      fetched.count(c => true) should be(1)
      fetched.head.value should be("myvalue")
      fetched.head.name should be("myname")
    }
    
    "not return a cookie for a different domain" in {
      val cookie = SBHttpCookie("myname", "myvalue", None)
      val newjar = jar.setCookie(cookie, "example2.org")
      val fetched = newjar.cookiesfor("www.example.org")
      fetched.count(c => true) should be(0)
    }
    
    "not return a cookie that has expired" in {
      val cookie = SBHttpCookie("myname", "myvalue", Some(new JT()))
      val newjar = jar.setCookie(cookie, "example.org")
      val fetched = newjar.cookiesfor("www.example.org")
      fetched.count(c => true) should be(0)
    }
    
    "return all cookies in scope" in {
      val cookie2 = SBHttpCookie("myname2", "myvalue", None)
      val cookie = SBHttpCookie("myname", "myvalue", None)
      var newjar = jar.setCookie(cookie2, "www.example.org")
      newjar = newjar.setCookie(cookie, "example.org")
      val fetched = newjar.cookiesfor("www.example.org")
      fetched.count(c => c.name == "myname2") should be (1)
      fetched.count(c => c.name == "myname") should be(1) 
    }
    
    "understand .prefixed domains" in {
      val cookie = SBHttpCookie("myname", "myvalue", None)
      val newjar = jar.setCookie(cookie, ".example.org")
      val fetched = newjar.cookiesfor("www.example.org")
      fetched.count(c => c.name == "myname") should be(1)
    }

  }


}