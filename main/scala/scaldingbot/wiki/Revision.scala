package scaldingbot.wiki

import org.joda.time.DateTime

case class Revision( id : Long, author : Editor, madeAt : DateTime)