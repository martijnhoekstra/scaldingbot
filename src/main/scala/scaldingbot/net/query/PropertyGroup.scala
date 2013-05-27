package scaldingbot.net.query

trait PropertyGroup {
  val name : String
  val prefix : String
  val abbriv = "prop"
  val switches : Set[String]
  def queryPairs : List[(String, String)]
  def switchpair =  ((prefix + "prop") , switches.toList.mkString("|"))
  def queryStrings = queryPairs.map(t => t._1 + "=" + t._2)

}
