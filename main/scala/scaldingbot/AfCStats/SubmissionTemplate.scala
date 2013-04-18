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
      
      getSubmission(params, letter)

    }
  }
  
  def getSubmission(params : Map[String, String], letter : String) = {
    val submitterName = params.get("u")
    val declineTimestamp = params.get("declinets")
    val decliner = params.get("decliner")
    val submittedTiemstamp=params.get("ts")
    val opending = getPending(params)
    letter match {
      case "" => opending
      case "d" => opending match {
        case None => None
        case Some(pending) => getDecline(params, pending)
      }
      case _ => None
    }
  }
  
  def getDecline(params : Map[String, String], pending : AfCPending) : Option[AfCDeclined] = {
    ???
  }
  

  def getPending(params : Map[String, String]) : Option[AfCPending]= {
    ???
  }
    
}