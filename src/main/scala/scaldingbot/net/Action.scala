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
import spray.http.Uri.{Query => SprayQuery}
import spray.http.Uri.Authority
import scala.collection.mutable.MutableList
import scaldingbot.settings.Settings
import scaldingbot.net.query.ApiPropertySet
import spray.httpx.SprayJsonSupport

trait Action[Response]{
  import system.dispatcher
  
  implicit val system : ActorSystem
  val authority : Authority
  val scheme : String = "https"
  val useragent = Settings.useragent
  val actiontype : ActionType
  val defaultproperties : ApiPropertySet
  implicit val rootformat : RootJsonFormat[Response]
  

      
  def createRequest(queryparams : List[(String, String)], body : Option[Array[Byte]] = None) = {
    def e(part :String) = URLEncoder.encode(part, "UTF-8")
    val querystring = queryparams.map(i => s"${e(i._1)}=${e(i._2)}").mkString("?", "&", "")
    val query = SprayQuery(queryparams.toMap)
    val uri = Uri(scheme, authority, Action.path, query, None)
    Post(uri, body)
  }
  
  import SprayJsonSupport._
  
  def perform(params : ApiPropertySet) = {
    
    val qpars = params ++ defaultproperties ++ Action.defaultParams + actiontype
    val request = createRequest(qpars.formatted)
    val pipeline: HttpRequest => Future[Response] = (
        addHeader("User-Agent", Action.useragent) ~>
        addHeaders(Action.cookieJar.toList) ~>
        sendReceive ~>
        storeCookies(Action.cookieJar) ~>
        unmarshal[Response]
        )
    pipeline(request)
  }
  
  def addHeaders(headers : List[HttpHeader]) = {
    headers.foldLeft(identity[HttpRequest] _ )((c, header)=> c andThen addHeader(header))
  }
  
  def storeCookies(cookiejar : MutableList[HttpHeader]) = {
    def res(r : HttpResponse) = {
      val cookieHeaders = r.headers.filter(_.name == "Set-Cookie")
      cookiejar ++= cookieHeaders
      r
    }
    res _
  }

}
    

object Action {
  val cookieJar : MutableList[HttpHeader] = MutableList()
  val useragent = s"Scaldingbot/0.2 (https://github.com/martijnhoekstra/scaldingbot/; ${Settings.email} - User:${Settings.user}) SprayClient/1.2-M8"
  val path = Path("/w/api.php")
  val defaultParams : ApiPropertySet = new ApiPropertySet(JSon :: Nil)
}


