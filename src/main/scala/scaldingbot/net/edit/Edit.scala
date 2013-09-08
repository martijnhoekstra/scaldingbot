package scaldingbot.net.edit

import scaldingbot.net.tokens.EditToken
import scaldingbot.net.ApiProperty
import scaldingbot.net.ApiPropertySet
import java.nio.charset.Charset

case class Edit(page: Either[String, Int], content: Content, conflictResolution: Option[Conflict]) {
  def toApiPropertySet = {
    def pageproperty = {
      case class TitleProperty(val value_ : String) extends ApiProperty {
        val name = "title"
        val value = Some(value_)
      }
      case class PageIdProperty(val value_ : Int) extends ApiProperty {
        val name = "title"
        val value = Some(value_.toString)
      }

      page match {
        case Left(title) => TitleProperty(title)
        case Right(id) => PageIdProperty(id)
      }
    }
    content.apiPropertySet + pageproperty
  }
}

case class Content(location: Option[Either[String, Int]], text: String, summary: String) {
  def md5: String = {
    val digest = java.security.MessageDigest.getInstance("MD5")
    digest.update(text.getBytes(Charset.forName("UTF-8"))) //ORLY? Not properly documented
    val hash = digest.digest()
    hash.map("%02X" format _).mkString
  }

  case class SectionProperty(val value_ : String) extends ApiProperty {
    val name = "section"
    val value = Some(value_)
  }

  case class SectionTitleProperty(val value_ : String) extends ApiProperty {
    val name = "sectiontitle"
    val value = Some(value_)
  }
  
  case class TextProperty(val value_ : String) extends ApiProperty {
    val name = "text"
    val value = Some(value_)
  }

  def apiPropertySet = {
    def loctionProperties: ApiPropertySet = {
      location match {
        case None => ApiPropertySet()
        case Some(Left(s)) => SectionProperty("new") + SectionTitleProperty(s.toString): ApiPropertySet
        case Some(Right(i))  => ApiPropertySet(SectionProperty(i.toString).toApiPropertyValueSet :: Nil): ApiPropertySet
      }
    }

    loctionProperties + TextProperty(text)
  }
}
case class Conflict(timestamp: Option[Timestamp], create: Option[PageCreation])
case class EditFlags(minoroverride: Option[Boolean])
abstract sealed class Timestamp extends ApiProperty {
}
sealed abstract class PageCreation extends ApiProperty {
  val value = None
}
case object recreate extends PageCreation {
  val name = "recreate"
}
case object createonly extends PageCreation {
  val name = "createonly"
}
case object nocreate extends PageCreation {
  val name = "nocreate"
}

object EditMarshaller {
  implicit def toApiPropertySet(edit: Edit) = {
    ApiPropertySet()
  }
}


/*

  title               - Title of the page you want to edit. Cannot be used together with pageid
  pageid              - Page ID of the page you want to edit. Cannot be used together with title
  section             - Section number. 0 for the top section, 'new' for a new section
  sectiontitle        - The title for a new section
  text                - Page content
  token               - Edit token. You can get one of these through prop=info.
                        The token should always be sent as the last parameter, or at least, after the text parameter
                        This parameter is required
  summary             - Edit summary. Also section title when section=new and sectiontitle is not set
  minor               - Minor edit
  notminor            - Non-minor edit
  bot                 - Mark this edit as bot
  basetimestamp       - Timestamp of the base revision (obtained through prop=revisions&rvprop=timestamp).
                        Used to detect edit conflicts; leave unset to ignore conflicts
  starttimestamp      - Timestamp when you obtained the edit token.
                        Used to detect edit conflicts; leave unset to ignore conflicts
  recreate            - Override any errors about the article having been deleted in the meantime
  createonly          - Don't edit the page if it exists already
  nocreate            - Throw an error if the page doesn't exist
  watch               - DEPRECATED! Add the page to your watchlist
  unwatch             - DEPRECATED! Remove the page from your watchlist
  watchlist           - Unconditionally add or remove the page from your watchlist, use preferences or do not change watch
                        One value: watch, unwatch, preferences, nochange
                        Default: preferences
  md5                 - The MD5 hash of the text parameter, or the prependtext and appendtext parameters concatenated.
                        If set, the edit won't be done unless the hash is correct
  prependtext         - Add this text to the beginning of the page. Overrides text
  appendtext          - Add this text to the end of the page. Overrides text.
                        Use section=new to append a new section
  undo                - Undo this revision. Overrides text, prependtext and appendtext
  undoafter           - Undo all revisions from undo to this one. If not set, just undo one revision
  redirect            - Automatically resolve redirects
  contentformat       - Content serialization format used for the input text
                        One value: text/x-wiki, text/javascript, text/css, text/plain, application/json
  contentmodel        - Content model of the new content
                        One value: wikitext, javascript, css, text, JsonZeroConfig, Scribunto, JsonSchema
  assert              - Allows bots to make assertions.
                         true   - Always true; nassert=true will fail if the extension is installed.
                         false  - Always false; assert=false will fail if the extension is installed.
                         user   - Verify that bot is logged in, to prevent anonymous edits.
                         bot    - Verify that bot is logged in and has a bot flag.
                         exists - Verify that page exists. Could be useful from other extensions, i.e. adding nassert=exists to the inputbox extension.
                         test   - Verify that this wiki allows random testing. Defaults to false, but can be overridden in LocalSettings.php.
                        One value: true, false, user, bot, exists, test
  nassert             - Allows bots to make negative assertions.
                         true   - Always true; nassert=true will fail if the extension is installed.
                         false  - Always false; assert=false will fail if the extension is installed.
                         user   - Verify that bot is logged in, to prevent anonymous edits.
                         bot    - Verify that bot is logged in and has a bot flag.
                         exists - Verify that page exists. Could be useful from other extensions, i.e. adding nassert=exists to the inputbox extension.
                         test   - Verify that this wiki allows random testing. Defaults to false, but can be overridden in LocalSettings.php.
                        One value: true, false, user, bot, exists, test
  captchaword         - Answer to the CAPTCHA
  captchaid           - CAPTCHA ID from previous request
  
  
  
  
  title: Page to edit. Cannot be used together with pageid.
pageid: Page ID of the page to edit. Cannot be used together with title.
section: nth section to edit. Use 0 for the top section, 'new' for a new section. Omit if replacing the entire page
sectiontitle: Title to use if creating a new section. If not specified, summary will be used instead MW 1.19+
text: New page (or section) content
token: Edit token. Especially if you are not using the md5 parameter, the token should be sent as the last parameter, or at least, after the text parameter, to prevent a bad edit from getting committed if transmission of the body is interrupted for some reason.
summary: Edit comment
minor: If set, mark the edit as minor [1]
notminor: If set, don't mark the edit as minor, even if you have the "Mark all my edits minor by default" preference enabled [1]
bot: If set, mark the edit as bot; even if you are using a bot account the edits will not be marked unless you set this flag [1]
basetimestamp: Timestamp of the last revision, used to detect edit conflicts. Leave unset to ignore conflicts
starttimestamp: Timestamp when you obtained the edit token. Used to detect edit conflicts. Leave unset and use recreate to ignore conflicts
recreate: If set, suppress errors about the page having been deleted in the meantime and recreate it [1]
createonly: If set, throw an error if the page already exists [1]
nocreate: If set, throw a missingtitle error if the page doesn't exist [1]
watchlist: Specify how the watchlist is affected by this edit, set to one of "watch", "unwatch", "preferences", "nochange":
watch: add the page to the watchlist
unwatch: remove the page from the watchlist
preferences: use the preference settings (Default)
nochange: don't change the watchlist
md5: MD5 hash (hex) of the text parameter. If this parameter is set and the hashes don't match, the edit is rejected. This can be used to guard against data corruption
captchaid: CAPTCHA ID from the previous request
captchaword: Answer to the CAPTCHA
undo: Revision ID to undo. Overrides text, prependtext and appendtext
undoafter: Undo all revisions from undo up to but not including this one. If not set, just undo one revision

*/