package scaldingbot.net

import dispatch._, Defaults._

trait Query {
  val useragent = "Scaldingbot/0.1 (http://example.com/NoUrlYet/; Martijnhoekstra@gmail.com - User:Martijn_Hoekstra en.wikipedia) ScalaDispatch/0.10.0"
  
  val action = "query"
  def base = host("en.wikipedia.org")
  def endpoint = base / "w" / "api.php"
  val defaultParams = Map("format" -> "json")
  
  
  def perform(extraParams : Map[String, String]) = {
    val queryparams = defaultParams ++ extraParams
    def mybase = endpoint.GET.addQueryParameter("action", action) <:< Map("user-agent" -> useragent)
    val query = queryparams.foldLeft(mybase)((q, kvp ) => q.addQueryParameter(kvp._1, kvp._2))
    println(query)
    val result = Http(query OK as.String)
    result()
  }

}