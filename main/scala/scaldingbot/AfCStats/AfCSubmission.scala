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
  val reviewer : Editor
  val reviewed : DateTime
}

trait AfCDeclined extends AfCReviewed
trait AfCAccpeted extends AfCReviewed

case class AfCPending(val submitter : Editor, val submitted : DateTime) extends AfCPendingLike