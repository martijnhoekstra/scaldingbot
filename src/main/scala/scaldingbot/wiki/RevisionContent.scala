package scaldingbot.wiki

trait RevisionContent {
  self => Revision 
  val content : String
}