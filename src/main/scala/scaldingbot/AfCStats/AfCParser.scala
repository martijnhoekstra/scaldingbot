package scaldingbot.AfCStats
import Stream._
import _root_.net.liftweb.json._

object AfCParser {
  case class Page(pageid : Long)
  case class Quer(categorymembers : Seq[Page])
  
  def parseArticleIds(jsonstring : String) = {
    implicit val formats = DefaultFormats
    val json = parse(jsonstring)
    val ids = (json \\ "query").extract[Quer].categorymembers.map(p => p.pageid)
    val querycontinue = (json \\ "query-continue" \\ "categorymembers")
    val continuestring :: none = (for { JField("cmcontinue", JString(s)) <- json } yield s)
    (continuestring, ids.toList)
  }
  
  def parseArticle(jsonstring : String) = {
    ???
  }

}