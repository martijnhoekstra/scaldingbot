package scaldingbot.net.query

class Revision(val switches : Set[String], properties : Set[(Property, String)], continue : Option[String]) extends PropertyGroup{
  val prefix = "rv"
  val name = "revision"
  def queryPairs = switchpair :: continue.map(c => ("continue" , c )).toList ::: properties.map(p => (prefix + p._1, p._2)).toList 
}