package scaldingbot.wiki

trait RevisionHistory[T <: Revision] {
 self => Article
 val revisions : List[T]
}