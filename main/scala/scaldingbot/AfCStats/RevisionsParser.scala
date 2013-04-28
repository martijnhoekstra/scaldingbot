package scaldingbot.AfCStats
import Stream._
import _root_.net.liftweb.json._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import scaldingbot.wiki.Revision
import scaldingbot.wiki.RevisionContent
import scaldingbot.wiki.Article
import scaldingbot.wiki.RevisionHistory
import scaldingbot.wiki.Namespace

class RevisionsParser(pageId : Long) {
  val pageIdString = pageId.toString
  case class FullQueryExtractor(`query-continue` : RevisionsContinueExtractor)
  case class RevisionsContinueExtractor(revisions : InnerRevisionConintueExtractor)
  case class InnerRevisionConintueExtractor(rvcontinue : Long)
  case class PageExtractor(ns : Int, title : String, pageId : Long, revisions : Seq[RevisionExtractor])
  case class RevisionExtractor(revid : Long, `*` : String, timestamp : String, user : String, userid : Int)
  
  def getArticleWithRevisions(jsonstring : String) = {
    implicit val formats = DefaultFormats
    val json = parse(jsonstring)
    val qc = json.extractOpt[FullQueryExtractor].map(fqe => fqe.`query-continue`.revisions.rvcontinue)
    val page = json \\ "query" \\ "pages" \\ pageIdString
    val p = page.extract[PageExtractor]
    val revisions = p.revisions.map(buildRevision)
    val article = buildArticle(p, revisions)
    (qc, article)
  }
  
  def buildArticle(page : PageExtractor, revs : Seq[Revision with RevisionContent]) = {
    new Article(page.title, Namespace(page.ns, "not implemented")) with RevisionHistory[Revision with RevisionContent] {
      val revisions = revs.toList
    }
  }
  
  def buildRevision(re : RevisionExtractor) = {
    val dt = DateTime.parse(re.timestamp, ISODateTimeFormat.dateTimeNoMillis())
    new Revision(re.revid, re.user, dt) with RevisionContent {
      val content = re.`*`
    }
  }
}