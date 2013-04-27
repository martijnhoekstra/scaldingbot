package scaldingbot

import dispatch._
import Defaults._
import Stream._
import scaldingbot.net._
import scaldingbot.AfCStats._
import scaldingbot.net.query._

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
    ids.map(buildArticle).map(perform)
    
  }
  
  def buildArticle(id : Long) = {
    val revprops = new Revision(
        ("timestamp" :: "content":: "user" :: "userid" :: Nil).toSet,
        Map[Property, String](Continue -> "", Limit -> "max", Dir -> "newer")
    )
    val revparams = revprops.queryPairs.map(p => p._1 -> p._2 )
        
    val params = Map[String, String]("ids" -> id.toString()) ++ revparams
    params
    
        
  }
  
  def buildRevision(id : Long) = {
    ???
  }
  
  

    

}