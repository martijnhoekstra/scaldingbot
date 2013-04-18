package scaldingbot

import dispatch._
import Defaults._
import Stream._
import scaldingbot.net._
import scaldingbot.AfCStats._
import scala.annotation.tailrec

object AfCStatsBot extends Query {
  
  def articles(queryparams : Map[String, String]) = {
      
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
    def buildPropString(props : List[String]) : String = {
      props match {
        case Nil => ""
        case head :: second :: tail => head + "|" + buildPropString(second :: tail)
        case head :: tail => head
      }
    }
    
    val catDeclined = "Category:Declined_AfC_submissions";
    val action = "query"
    val list = "categorymembers"
    val props = "ids" :: Nil
    val call = Map("list" -> list, "cmtitle" -> catDeclined, "cmprop" -> buildPropString(props))
    articles(call)
    
    
  }
  
  

    

}