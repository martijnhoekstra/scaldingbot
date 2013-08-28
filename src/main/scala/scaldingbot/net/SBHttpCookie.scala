package scaldingbot.net

import org.joda.time.DateTime
import spray.http.HttpHeaders.Host
import spray.http.Uri
import scala.annotation.tailrec

/**
 * This is a *basic* CookieJar implementation. It will happily accept cookies with invalid characters
 * accept cookies without domain (which will happily be served to *any* site), accept cookies for
 * top level domains, and cookies for invalid domains. As such, it is not suitable for general use
 * where privacy matters are important, and not suitable for use where you may expect "bad" cookies.
 * 
 *  Also, cookie paths, ports, http directives, and secure directives are completely ignored.
 *  Emptor caveat. 
 */
case class CookieJar(domainElement: String, subdomains : Map[String, CookieJar], cookies : Set[SBHttpCookie]){
  def cookiesfor(domain : String) = {
    val domainelements = domain.split('.').toList.reverse
    _getCookies(domainelements, Set.empty)
  }
  
  @tailrec
  private def _getCookies(domain : List[String], accum : Set[SBHttpCookie]) : Set[SBHttpCookie] = {
    val now = new DateTime()
    val directCookies = removeStale(cookies, now)
    domain match {
      case Nil => directCookies
      case head :: tail => {
        subdomains.get(head) match {
          case None => directCookies
          case Some(jar) => jar._getCookies(tail, directCookies)
        }
      }
    }
  }
  
  def cookieString(domain : String)  = {
    cookiesfor(domain).mkString("; ")
  }
  
  def setCookie(cookie : SBHttpCookie, domain : String) = {
    val domainelements = domain.split('.').toList.reverse
    _setCookie(domainelements, cookie)
  }
  
  private def _setCookie(domain : List[String], cookie : SBHttpCookie) : CookieJar = {
    val now = new DateTime()
      domain match {
      case Nil => {
        val newcookies =  removeStale(cookies, now).filterNot(c => c.name == cookie.name) + cookie
        this.copy(cookies = newcookies)
      }
      case head :: tail => {
        lazy val newsubjar = CookieJar(head, Map.empty, Set.empty)
        val subjar = subdomains.getOrElse(head, newsubjar)
        this.copy(subdomains = subdomains + (head -> subjar._setCookie(tail, cookie)))
      }
    }
    
  }
  
  def removeStale(cookies : Set[SBHttpCookie], cutoff : DateTime) = {
    cookies.filter(c => {
      c.expires match {
        case None => true
        case Some(datetime) if datetime.isAfter(cutoff) => true
        case _ => false
      }
    })
  }
}

case class SBHttpCookie(name : String, value : String, expires : Option[DateTime]) {
  override def toString = name + "=" + value
}
