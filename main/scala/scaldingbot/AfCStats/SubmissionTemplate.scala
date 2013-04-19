package scaldingbot.AfCStats

import scala.util.matching.Regex
import scala.collection.immutable.HashMap
import scaldingbot.wikitypes.Editor
import org.joda.time.DateTime

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
    val opending = getPending(params)
    letter match {
      case "" => opending
      case "d" => opending match {
        case None => None
        case Some(pending) => getDecline(pending, params)
      }
      case _ => None
    }
  }
  
  def getDecline(pending : AfCPending, params : Map[String, String]) : Option[AfCDeclined] = {
    val declineTimestamp = params.get("declinets")
    val declinero = params.get("decliner")
    if (declineTimestamp.isEmpty || declinero.isEmpty) None
    else {
      val decliner = Editor.fromName(declinero.get)
      val declineTime = new DateTime(declineTimestamp.get.toInt * 1000L)
      Some(AfCDeclined(pending, decliner, declineTime))
    }
    
  }
  

  def getPending(params : Map[String, String]) : Option[AfCPending]= {
    val submitterName = params.get("u")
    val submittedTimestamp=params.get("ts")
    if (submitterName.isEmpty || submitterName.isEmpty) None
    else {
      val submitter = Editor.fromName(submitterName.get)
      val submissionTime = new DateTime(submittedTimestamp.get.toInt * 1000L)
      Some(AfCPending(submitter, submissionTime))
    }
    
    
    
  }
    
}