package scaldingbot.net.query

/**
 * Returns revisions for a given page, or the latest revision for each of several pages.
 * When using parameters marked as (enum), titles= must have only one title listed.
 */
class Revision(val switches : Set[String], properties : Map[Property, String]) extends PropertyGroup{
  val prefix = "rv"
  val name = "revisions"
  def queryPairs = switchpair :: properties.map(p => (prefix + p._1, p._2)).toList 
}


/**
 * When more results are available, use this to continue. This can be used, for example, 
 * for fetching the text of all revisions of a page (although an XML export might be more efficient).
 */
case object Continue extends Property(name = "continue")
/**
 * The maximum number of revisions to return. Use the string "max" to return all revisions
 * (subject to being broken up as usual, using continue). Limited by query limits defined in ApiBase, 
 * which equals 500 for users and 5000 for bots. (enum)
 */ 
case object Limit extends Property(name = "limit")
/**
 *  Revision ID to start listing from. (enum)
 */
case object StartId extends Property(name= "startid")
/**
 * Revision ID to stop listing at. (enum)
 */
case object EndId extends Property(name = "endid")
/**
 *  Timestamp to start listing from. (enum)
 */
case object Start extends Property(name= "start")
/**
 * Timestamp to end listing at. (enum)
 */
case object End extends Property(name = "end")
/**
 * Direction to list in. Allowed values are "older" and "newer".
 * older: List newest revisions first (default) NOTE: start/startid has to be higher than end/endid
 * newer: List oldest revisions first NOTE: Start/StartId has to be lower than End/EndId
 */ 
case object Dir extends Property(name = "dir")
/**
 * Only list revisions made by this user
 */
case object User extends Property(name = "user")
/**
 * Do not list revisions made by this user
 */
case object ExcludeUser extends Property(name = "excludeuser")
/**
 * Expand templates in rvprop=content output
 */
case object ExpandTemplates extends Property(name = "expandtemplates")
/**
 * Generate XML parse tree for revision content
 */
case object GenerateXml extends Property(name = "generatexml")
/**
 * Parse revision content. For performance reasons if this option is used, rvlimit is enforced to 1.
 */
case object Parse extends Property(name = "parse")
/**
 * If rvprop=content is set, only retrieve the contents of this section. This is an integer, 
 * not a string title. MW 1.13+
 */
case object Section extends Property(name = "section")
/**
 * Tokens to get for each revision. only allowable value = rollback
 */
case object Tokens extends Property(name = "token") 
/**
 * Revision ID to diff each revision to. Use "prev", "next" and "cur" for the previous, next and 
 * current revision respectively.
 */
case object DiffTo extends Property(name = "diffto")
/**
 * Text to diff each revision to. Only diffs a limited number of revisions. Overrides Diffto. 
 * If Section is set, only that section will be diffed against this text.
 */
case object DiffToText extends Property(name = "difftotext") 