package scaldingbot.net

import scaldingbot.settings.Settings
import scaldingbot.net.query.ApiPropertyValueSet
import spray.http._
import spray.client.pipelining._
import scala.concurrent.Future
import akka.actor.ActorSystem
import java.net
import java.net.URLEncoder
import spray.json._
import DefaultJsonProtocol._
import spray.http.Uri.Path
import spray.http.Uri.{ Query => SprayQuery }
import spray.http.Uri.Authority
import scala.collection.mutable.MutableList
import scaldingbot.settings.Settings
import scaldingbot.net.query.ApiPropertySet
import spray.httpx.SprayJsonSupport
import spray.http.HttpHeaders.Cookie
import spray.http.HttpHeaders.`Set-Cookie`
import org.joda.time.{ DateTime => JT }

trait Action[Response] {
  import system.dispatcher

  implicit val system: ActorSystem
  val authority: Authority
  val scheme: String = "https"
  val useragent = Settings.useragent
  val actiontype: ActionType
  val defaultproperties: ApiPropertySet
  var cookiejar: CookieJar = CookieJar("", Map.empty, Set.empty)
  implicit val rootformat: RootJsonFormat[Response]

  def createRequest(queryparams: List[(String, String)], body: Option[Array[Byte]] = None) = {
    def e(part: String) = URLEncoder.encode(part, "UTF-8")
    val querystring = queryparams.map(i => s"${e(i._1)}=${e(i._2)}").mkString("?", "&", "")
    val query = SprayQuery(queryparams.toMap)
    val uri = Uri(scheme, authority, Action.path, query, None)
    Post(uri, body)
  }

  import SprayJsonSupport._

  def perform(params: ApiPropertySet) = {

    val qpars = params ++ defaultproperties ++ Action.defaultParams + actiontype
    val request = createRequest(qpars.formatted)
    val domain = request.uri.authority.host.address
    val pipeline: HttpRequest => Future[Response] = (
      addHeader("User-Agent", useragent) ~>
      withCookies(cookiejar) ~>
      sendReceive ~>
      //Mind the side effects!
      storeCookies(domain) ~>
      unmarshal[Response]
    )
    pipeline(request)
  }

  def addHeaders(headers: List[HttpHeader]) = {
    headers.foldLeft(identity[HttpRequest] _)((c, header) => c andThen addHeader(header))
  }

  def withCookies(jar: CookieJar) = {
    def res(r: HttpRequest) = {
      val mycookies = jar.cookiesfor(r.uri.authority.host.address)
      val realcookies = mycookies.map(mk => HttpCookie(mk.name, mk.value, mk.expires.map(dt => DateTime(dt.toInstant().getMillis()))))
      val cookieheader = Cookie(realcookies.toList)
      addHeader(cookieheader)(r)
    }
    res _
  }

  /**
   * Mind the side effects!
   */
  def storeCookies(domain: String) = {
    def res(r: HttpResponse) = {
      val cookieHeaders = r.headers collect { case c: `Set-Cookie` => c }
      for (c <- cookieHeaders.map(ch => ch.cookie)) {
        val cookiedomain = c.domain.getOrElse(domain)
        val ct = c.expires.map(ex => new JT(ex))
        cookiejar = cookiejar.setCookie(SBHttpCookie(c.name, c.content, ct), cookiedomain)
      }
      r
    }
    res _
  }
  
  trait withCookies {
    final var _jar = CookieJar("", Map.empty, Set.empty)
    def sr(domain : String) = {
      withCookies(cookiejar) ~>
      sendReceive ~>
      //Mind the side effects!
      storeCookies(domain)
    }
  }

}

object Action {
  val cookieJar: MutableList[HttpHeader] = MutableList()
  val path = Path("/w/api.php")
  val defaultParams: ApiPropertySet = new ApiPropertySet(JSon :: Nil)
}