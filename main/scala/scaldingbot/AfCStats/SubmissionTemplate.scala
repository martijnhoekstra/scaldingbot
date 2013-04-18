package scaldingbot.AfCStats

import scala.util.matching.Regex
import scala.collection.immutable.HashMap

class SubmissionTemplate {
   // submitted: {{AFC submission| | |ts=20130218044659       |u=Alex glen richards|ns=2}}
   // declined:  {{AFC submission|d|v|declinets=20130418162652|decliner=Fumitol    |ts=20130412055708|u=Alex glen richards|ns=5|small=yes}}
  val pattern = new Regex("""{{AFC submission\|(\l?)\|([^}]*)}}""", "status", "rest")
  
  def getSubmissionData(content : String) = {
    val matches = pattern.findAllMatchIn(content)
    for (pattern(letter, rest) <- pattern findAllIn content) yield {
      val params = rest.split("|").foldLeft(new HashMap[String, String]())((map, content) => {
        val kv = content.split("=")
        if (kv.length == 2) map + (kv(0) -> kv(1))
        else map
      })

      letter match {
        case "d" => getDeclined(params)
        case "" => getSubmitted(params)
        case _ => None
      }
    }
  }
  
  def getDeclined(params : Map[String, String]) = {
    None
  }
  
  def getSubmitted(params : Map[String, String]) = {
    None
  }
  
}