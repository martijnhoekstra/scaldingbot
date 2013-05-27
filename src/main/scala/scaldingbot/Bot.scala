package scaldingbot

import scaldingbot.wiki.Revision
import scaldingbot.wiki.Article
import scaldingbot.AfCStats.AfCSubmission
import scaldingbot.wiki.RevisionHistory
import scaldingbot.wiki.RevisionContent
import scaldingbot.AfCStats.AfCDeclined

object Bot {

  def main(args: Array[String]): Unit = {
    
    val articles = AfCStatsBot.getArticles.take(1000)
    val res = articles.collect { case Some(t) =>  basicstats(t) } 
    res.foreach(println)
    val agg = res.foldLeft(Map.empty : Map[Option[String], Int])((t, m) => sumMergeMap(t, m._4))
    println(agg)
    val stop = 5 
  }
  
    def sumMergeMap[T](some : Map[T, Int], other : Map[T, Int]) = {
      some.foldLeft(other) ((t, n) => t + ((n._1, n._2 + t.getOrElse(n._1, 0) )))
    }
  
  def basicstats(a : (Article with RevisionHistory[Revision with RevisionContent], Map[Revision with RevisionContent, Set[AfCSubmission]])) = {
      val numedits = a._1.revisions.count(_ => true)
      val declines = a._2.values.foldLeft(Set.empty : Set[AfCDeclined])( (t, s) => t ++ s collect { case d : AfCDeclined => d } )
      val numdeclines = declines.count( _ => true)
      val reasons = declines.groupBy(d => d.reason).map(t =>( t._1,  t._2.count( _ => true) ))
      (a._1.title, numedits, numdeclines, reasons)
    }

}