package scaldingbot

import dispatch._
import Defaults._
import Stream._
import scaldingbot.net._
import scaldingbot.AfCStats._
import scaldingbot.wiki.RevisionHistory
import scala.annotation.tailrec
import scaldingbot.wiki.Article
import scaldingbot.wiki.RevisionContent
import scaldingbot.net.query.RevisionQuery
import scaldingbot.net.query.Property
import scaldingbot.net.query.Dir
import scaldingbot.net.query.Continue
import scaldingbot.net.query.Limit


object AfCStatsBot extends Query {
  
  def articleIds(queryparams : Map[String, String]) = {
      
      def _next(continue : String, current : List[Long], first : Boolean ) : Stream[Long]  = {
        current match {
          case head :: tail => head #:: _next(continue, tail, false)
          case Nil if first => empty
          case _ => {
            val (nc, nl) = getIds(continue)
            _next(nc, nl, true)
          }
        }
      }
      
      def getIds(continue : String) = {
        val jsonstring = perform(queryparams ++ Map("cmcontinue" -> continue))
        AfCParser.parseArticleIds(jsonstring)
      }
      
      val (continue, initial) = getIds("")
      _next(continue, initial, true)
    }
  
  def getArticles = {
    
    val catDeclined = "Category:Declined_AfC_submissions";
    val list = "categorymembers"
    val props = "ids" :: Nil
    val call = Map("list" -> list, "cmtitle" -> catDeclined, "cmprop" -> props.mkString("|"))
    val ids = articleIds(call)
    val mylist = ids.map(buildArticle(_))
    mylist  
  }
  
  def buildArticle(id : Long) = {
    val revprops = new RevisionQuery(
        ("timestamp" :: "content":: "user" :: "userid" :: "ids" :: Nil).toSet,
        Map[Property, String](Continue -> "", Limit -> "max", Dir -> "newer")
    )
    val revparams = revprops.queryPairs.map(p => p._1 -> p._2 )
        
    val params = Map[String, String]("pageids" -> id.toString()) ++ revparams
    val response = perform(params)
    val revisions = buildRevisions(response, id)
    revisions match {
      case (Some(continue), arts) => ??? //do the query continue
      case (None, arts) => (arts, associateSubmissionData(arts))
    }
  }
  
  def buildRevisions(json : String, id : Long) = {
     val parser = new RevisionsParser(id)
     val res = parser.getArticleWithRevisions(json)
     res
  }
  
   def associateSubmissionData(article : Article with RevisionHistory[scaldingbot.wiki.Revision with RevisionContent]) = {
    val all = article.revisions.map(r => SubmissionTemplate.getSubmissionData(r.content).toSet)
    
    @tailrec
    def filterSeen(seen : Set[AfCSubmission], src : List[Set[AfCSubmission]], acc : List[Set[AfCSubmission]]) : List[Set[AfCSubmission]]= {
      src match {
        case Nil => acc
        case head :: tail => filterSeen(seen ++ head, tail, head -- seen :: acc)
      }
    }
    val filtered = filterSeen(Set.empty, all, Nil).reverse
    val mapped = article.revisions.zip(filtered).foldLeft(Map.empty : Map[scaldingbot.wiki.Revision with RevisionContent, Set[AfCSubmission]])((m, t) => m + (t._1 -> t._2))
    mapped
    
  }
      

}