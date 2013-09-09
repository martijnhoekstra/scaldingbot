package scaldingbot.net

import scala.collection.mutable.MutableList
import scala.concurrent.Future
import akka.actor.ActorSystem
import scaldingbot.settings.Settings
import spray.client.pipelining.Post
import spray.client.pipelining.WithTransformerConcatenation
import spray.client.pipelining.addHeader
import spray.client.pipelining.sendReceive
import spray.client.pipelining.unmarshal
import spray.http.HttpCookie
import spray.http.HttpHeader
import spray.http.HttpHeaders.Cookie
import spray.http.HttpHeaders.`Set-Cookie`
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.http.Uri
import spray.http.Uri.Authority
import spray.http.Uri.Path
import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller
import spray.json.RootJsonFormat
import spray.httpx.SprayJsonSupport
import spray.httpx.marshalling.Marshaller
import spray.http.FormData
import spray.http.ContentType._
import spray.http._
import MediaTypes._

trait Action[Response] {
  import system.dispatcher

  implicit val system: ActorSystem
  val authority: Authority
  val scheme: String = "https"
  val useragent = Settings.useragent
  val actiontype: ActionType
  val defaultproperties: ApiPropertySet
  implicit val rootformat: RootJsonFormat[Response]
  implicit val UTF8FormDataMarshaller =
    Marshaller.delegate[FormData, String](`application/x-www-form-urlencoded`) { (formData, contentType) ⇒
      import java.net.URLEncoder.encode
      val charset = "UTF-8"
      formData.fields.map { case (key, value) ⇒ encode(key, charset) + '=' + encode(value, charset) }.mkString("&")
    }

  def createRequest[T](body: Option[T] = None)(implicit evidence: spray.httpx.marshalling.Marshaller[T]) = {
    val uri = Uri(scheme, authority, Action.path)
    Post(uri, body)
  }

  import SprayJsonSupport._

  def perform[T](arg: T)(implicit marshaller: T => ApiPropertySet): Future[Response] = {
    perform(marshaller(arg))
  }

  def perform(params: ApiPropertySet): Future[Response] = {
    val qpars = Action.defaultParams ++ defaultproperties + actiontype ++ params 
    val request = createRequest(Some(qpars.asFormUrlEncoded))
    val domain = request.uri.authority.host.address
    val pipeline: HttpRequest => Future[Response] = (
      addHeader("User-Agent", useragent) ~>
      withCookies(Action.cookiejar) ~>
      sendReceive ~>
      //Mind the side effects!
      storeCookies(domain) ~>
      unmarshal[Response])
    pipeline(request)
  }

  def addHeaders(headers: List[HttpHeader]) = {
    headers.foldLeft(identity[HttpRequest] _)((c, header) => c andThen addHeader(header))
  }

  def withCookies(jar: CookieJar) = {
    def res(r: HttpRequest) = {
      val mycookies = jar.cookiesfor(r.uri.authority.host.address)
      val cookieheader = Cookie(mycookies.toList)
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
        Action.cookiejar = Action.cookiejar.setCookie(c, cookiedomain)
      }
      r
    }
    res _
  }

}

object Action {
  val path = Path("/w/api.php")
  val defaultParams: ApiPropertySet = new ApiPropertySet(JSon :: Nil)
  var cookiejar: CookieJar = CookieJar("", Map.empty, Set.empty)
}