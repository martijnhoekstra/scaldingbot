package scaldingbot

object Bot {

  def main(args: Array[String]): Unit = {
    val articles = AfCStatsBot.getArticles.take(5)
    articles.foreach(println)
    val stop = 5

    
  }

}