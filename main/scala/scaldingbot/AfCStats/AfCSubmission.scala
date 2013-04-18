package scaldingbot.AfCStats

import org.joda.time.DateTime
import scaldingbot.wikitypes._

abstract class AfCSubmission
case class AfCPending (submitter : Editor, submitted : DateTime, submittedIn : Namespace) extends AfCSubmission
case class AfCDeclined (submission : AfCPending, reviewer : Editor, declined : DateTime, declinedIn : Namespace) extends AfCSubmission
case class AfCAccepted (submission : AfCPending, reviewer : Editor, accepted : DateTime, acceptedIn : Namespace) extends AfCSubmission