package scaldingbot.net.query

import scaldingbot.net.ApiPropertyValueSet

/**
 * Returns revisions for a given page, or the latest revision for each of several pages.
 * When using parameters marked as (enum), titles= must have only one title listed.
 */
class RevisionQuery(val switches : Set[String], properties : Map[ApiPropertyValueSet, String]) {
  val prefix = "rv"
  val name = "revisions"
  //def queryPairs = "prop" -> name :: switchpair :: properties.map(p => (prefix + p._1.name, p._2)).toList 
}


/**
 * When more results are available, use this to continue. This can be used, for example, 
 * for fetching the text of all revisions of a page (although an XML export might be more efficient).
 */
trait Continue extends ApiPropertyValueSet{
  val name = "continue"
}
/**
 * The maximum number of revisions to return. Use the string "max" to return all revisions
 * (subject to being broken up as usual, using continue). Limited by query limits defined in ApiBase, 
 * which equals 500 for users and 5000 for bots. (enum)
 */ 
trait Limit extends ApiPropertyValueSet{
  val name = "limit"
}
/**
 *  Revision ID to start listing from. (enum)
 */
trait StartId extends ApiPropertyValueSet{
  val name= "startid"
}
/**
 * Revision ID to stop listing at. (enum)
 */
trait EndId extends ApiPropertyValueSet {
  val name = "endid"
}
/**
 *  Timestamp to start listing from. (enum)
 */
trait Start extends ApiPropertyValueSet{
  val name= "start"
}
/**
 * Timestamp to end listing at. (enum)
 */
trait End extends ApiPropertyValueSet { 
  val name = "end"
}

/**
 * Direction to list in. Allowed values are "older" and "newer".
 * older: List newest revisions first (default) NOTE: start/startid has to be higher than end/endid
 * newer: List oldest revisions first NOTE: Start/StartId has to be lower than End/EndId
 */ 
trait Dir extends ApiPropertyValueSet { 
  val name = "dir"
}
/**
 * Only list revisions made by this user
 */
trait User extends ApiPropertyValueSet { 
  val name = "user"
}
/**
 * Do not list revisions made by this user
 */
trait ExcludeUser extends ApiPropertyValueSet { 
  val name = "excludeuser"
}
/**
 * Expand templates in rvprop=content output
 */
trait ExpandTemplates extends ApiPropertyValueSet { 
  val name = "expandtemplates"
}
/**
 * Generate XML parse tree for revision content
 */
trait GenerateXml extends ApiPropertyValueSet { 
  val name = "generatexml"
}
/**
 * Parse revision content. For performance reasons if this option is used, rvlimit is enforced to 1.
 */
trait Parse extends ApiPropertyValueSet { 
  val name = "parse"
}
/**
 * If rvprop=content is set, only retrieve the contents of this section. This is an integer, 
 * not a string title. MW 1.13+
 */
trait Section extends ApiPropertyValueSet { 
  val name = "section"
}
/**
 * Tokens to get for each revision. only allowable value = rollback
 */
trait Tokens extends ApiPropertyValueSet { 
  val name = "token"
}
/**
 * Revision ID to diff each revision to. Use "prev", "next" and "cur" for the previous, next and 
 * current revision respectively.
 */
trait DiffTo extends ApiPropertyValueSet { 
  val name = "diffto"
}
/**
 * Text to diff each revision to. Only diffs a limited number of revisions. Overrides Diffto. 
 * If Section is set, only that section will be diffed against this text.
 */
trait DiffToText extends ApiPropertyValueSet { 
  val name = "difftotext"
}