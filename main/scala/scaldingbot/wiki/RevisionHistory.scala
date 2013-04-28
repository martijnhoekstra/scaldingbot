package scaldingbot.wiki

trait RevisionHistory[T <: Revision] {
 val revisions : List[T]
}