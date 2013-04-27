package scaldingbot.AfCStats

import org.joda.time.DateTime
import scaldingbot.wiki._

abstract class AfCSubmission
case class AfCPending (submitter : Editor, submitted : DateTime) extends AfCSubmission
case class AfCDeclined (submission : AfCPending, reviewer : Editor, declined : DateTime) extends AfCSubmission
case class AfCAccepted (submission : AfCPending, reviewer : Editor, accepted : DateTime) extends AfCSubmission