package scaldingbot.AfCStats

import scala.util.matching.Regex
import scala.collection.immutable.HashMap
import scaldingbot.wiki.Editor
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

object SubmissionTemplate {
   // submitted: {{AFC submission| | |ts=20130218044659       |u=Alex glen richards|ns=2}}
   // declined:  {{AFC submission|d|v|declinets=20130418162652|decliner=Fumitol    |ts=20130412055708|u=Alex glen richards|ns=5|small=yes}}
  val pattern = new Regex("""\{\{AFC submission\|([^|]?)\|([^}]*)\}\}""", "status", "rest")
  
  def getSubmissionData(content : String) = {
    val matches = pattern.findAllMatchIn(content)
    for (pattern(letter, rest) <- pattern findAllIn content) yield {
      val params = rest.split("""\|""").foldLeft(new HashMap[String, String]())((map, content) => {
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
      val declineTime = parseDateTime(declineTimestamp.get).get
      val declined = new AfCDeclined{
        val submission = pending
        val reviewer = decliner
        val reviewed = declineTime
      }
      Some(declined)
    }
    
  }
  

  def getPending(params : Map[String, String]) : Option[AfCPending]= {
    val submitterName = params.get("u")
    val submittedTimestamp=params.get("ts")
    if (submitterName.isEmpty || submitterName.isEmpty) None
    else {
      val submitter = Editor.fromName(submitterName.get)
      val submissionTime = parseDateTime(submittedTimestamp.get).get
      val pending = new AfCPending(submitter, submissionTime)
      Some(pending)
    }
    
  }
  
  def parseDateTime(datestring : String) = {
    try {
      val year = datestring.substring(0, 4).toInt
      val month = datestring.substring(4, 6).toInt
      val day = datestring.substring(6,8).toInt
      val hour = datestring.substring(8,10).toInt
      val minute = datestring.substring(10, 12).toInt
      val second = datestring.substring(12, 14).toInt
      Some(new DateTime(year, month, day, hour, minute, second, 0, DateTimeZone.UTC))
    } catch {
      case _  : Throwable => None
    }
  }
    
}