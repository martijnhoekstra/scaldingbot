package scaldingbot.AfCStats

import org.joda.time.DateTime
import scaldingbot.wiki._

trait AfCSubmission
trait AfCPendingLike extends AfCSubmission {
  val submitter : Editor
  val submitted : DateTime
}

trait AfCReviewed extends AfCSubmission {
  val submission : AfCPending
  val reviewer : Option[Editor]
  val reviewed : Option[DateTime]
}

trait AfCDeclined extends AfCReviewed {
  val reason : Option[String]
}
trait AfCAccpeted extends AfCReviewed

case class AfCPending(val submitter : Editor, val submitted : DateTime) extends AfCPendingLike