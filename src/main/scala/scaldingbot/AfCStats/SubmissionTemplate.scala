package scaldingbot.AfCStats

import scala.util.matching.Regex
import scala.collection.immutable.HashMap
import scaldingbot.wiki.Editor
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import scaldingbot.wiki.Article
import scaldingbot.wiki.RevisionHistory
import scaldingbot.wiki.RevisionContent
import scaldingbot.wiki.Revision
import scala.annotation.tailrec

object SubmissionTemplate {
  // submitted: {{AFC submission| | |ts=20130218044659       |u=Alex glen richards|ns=2}}
  // declined:  {{AFC submission|d|v|declinets=20130418162652|decliner=Fumitol    |ts=20130412055708|u=Alex glen richards|ns=5|small=yes}}
  val pattern = new Regex("""\{\{AFC submission\|([^|]?)\|([^|]*)\|([^}]*)\}\}""", "status", "rest")
  def getSubmissionData(content: String) = {
    val matches = pattern.findAllMatchIn(content)
    val it =
      for (pattern(letter, reason, rest) <- pattern findAllIn content) yield {
        val params = rest.split("""\|""").foldLeft(Map("reason" -> reason))((map, content) => {
          val kv = content.split("=")
          if (kv.length == 2) map + (kv(0) -> kv(1))
          else map
        })
        getSubmission(params, letter)
      }
    it collect { case Some(submission) => submission }
  }

  def getSubmission(params: Map[String, String], letter: String) = {
    val opending = getPending(params)
    if (letter == "") opending
    else if (letter == "d" || letter == "D") opending match {
        case None => None
        case Some(pending) => getDecline(pending, params)
      }
    else None
  }

  def getDecline(pending: AfCPending, params: Map[String, String]): Option[AfCDeclined] = {
    val declineTimestamp = params.get("declinets")
    val declinero = params.get("decliner")
    val decliner = declinero.map(d => Editor.fromName(d))
    val declineTime = declineTimestamp.flatMap(d => parseDateTime(d))
    val declineReason = params.get("reason")
    val declined = new AfCDeclined {
      val submission = pending
      val reviewer = decliner
      val reviewed = declineTime
      val reason = declineReason
    }
    Some(declined)
  }

  def getPending(params: Map[String, String]): Option[AfCPending] = {
    val submitterName = params.get("u").flatMap( s => Some(Editor.fromName(s) ))
    val submittedTimestamp = params.get("ts").flatMap(s => parseDateTime(s))
    (submitterName, submittedTimestamp) match {
      case (Some(submitter), Some(submitted)) => Some (new AfCPending(submitter, submitted))
      case _ => None
    }
  }

  def parseDateTime(datestring: String) = {
    try {
      val year = datestring.substring(0, 4).toInt
      val month = datestring.substring(4, 6).toInt
      val day = datestring.substring(6, 8).toInt
      val hour = datestring.substring(8, 10).toInt
      val minute = datestring.substring(10, 12).toInt
      val second = datestring.substring(12, 14).toInt
      Some(new DateTime(year, month, day, hour, minute, second, 0, DateTimeZone.UTC))
    } catch {
      case _: Throwable => None
    }
  }

}