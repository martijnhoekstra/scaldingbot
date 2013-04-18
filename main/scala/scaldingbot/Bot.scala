package scaldingbot

object Bot {

  def main(args: Array[String]): Unit = {
    println(AfCStatsBot.getArticles.toList)
  }

}