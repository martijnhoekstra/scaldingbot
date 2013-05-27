package test.scaldingbot.AfCStats

import org.scalatest._
import scaldingbot.AfCStats.RevisionsParser
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import scaldingbot.wiki.Editor
import scaldingbot.wiki.Registered

class RevisionParserTest extends FunSpec {
  describe("A RevisionsParser") {
    it("should parse an article") {
      val jsonstring = """{ "limits" : { "revisions" : 5000 },
  "query" : { "pages" : { "37590947" : { "ns" : 5,
              "pageid" : 37590947,
              "revisions" : [ { "*" : "content",
                    "revid" : 123456,
                    "contentformat" : "text/x-wiki",
                    "contentmodel" : "wikitext",
                    "timestamp" : "2012-11-09T20:03:37Z",
                    "user" : "Oleksii777",
                    "userid" : 17858732
                  },
                  { "*" : "content",
                    "revid" : 123457,
                    "contentformat" : "text/x-wiki",
                    "contentmodel" : "wikitext",
                    "timestamp" : "2012-11-09T20:04:09Z",
                    "user" : "Oleksii777",
                    "userid" : 17858732
                  }
                ],
              "title" : "mytitle"
            } } }
}"""

      val parser = new RevisionsParser(37590947l)
      parser.getArticleWithRevisions(jsonstring) match {

        case None => assert(false, "failed to parse json")
        case Some((continue, article)) => {
          assert(article.title == "mytitle")
          assert(article.namespace.id == 5)
          val revisions = article.revisions
          assert(revisions.length == 2)
          assert(revisions.forall(r => r.author == Registered(0, "Oleksii777")))
        }
      }

    }

    it("should parse a continue string") {

    }
  }
}