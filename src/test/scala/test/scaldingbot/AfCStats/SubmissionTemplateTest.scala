package test.scaldingbot.AfCStats

import org.scalatest._
import scaldingbot.AfCStats.SubmissionTemplate
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import scaldingbot.AfCStats.AfCPending
import scaldingbot.wiki.Editor
import scaldingbot.wiki.Editor

class SubmissionTemplateTest extends FunSpec {
  describe("An SubmissionTemplate address") {
    
    it("should be able to parse a pending template") {
      val pendingString = """{{AFC submission||nn|ts=20130420034914|u=71.94.235.210|ns=5|small=yes}}"""
      SubmissionTemplate.getSubmissionData(pendingString).toList.headOption match {
        
        case Some(pending : AfCPending)  => {
          val expectedSubmission = SubmissionTemplate.parseDateTime("20130420034914").get
          assert(expectedSubmission == (pending.submitted), s"parsed submission date ${pending.submitted} not equal to expected submission date $expectedSubmission")
          val expectedEditor = Editor.fromName("71.94.235.210")
          assert(expectedEditor == pending.submitter, "submitter not equal to expected submitter")
        }
        case None => assert(false, "failed to parse template to AfC Pending")
        case Some(other) => assert(false, "failed to parse template to AfC Pending, got " + other.getClass().getName() + " instead")
      }
    }
    
    
    it("should be able to parse templates in an article") {
      val pagecontent = """
        {{AFC submission||nn|ts=20130420034914|u=71.94.235.210|ns=5|small=yes}}{{AFC submission|d|v|declinets=20130406050345|decliner=Anne Delong|ts=20130405194707|u=71.94.235.210|ns=5}}
{{afc comment|1=Declining due to lack of reliable sources and apparent lack of notability. [[User:davidwr|davidwr]]/<small><small>([[User_talk:davidwr|talk]])/([[Special:Contributions/Davidwr|contribs]])/([[Special:Emailuser/davidwr|e-mail]])</small></small> 03:25, 21 April 2013 (UTC)}}

{{afc comment|1=Please find sources such as maps, gazetteers, news reports or books to vrify the information in this article. &mdash;[[User:Anne Delong|Anne Delong]] ([[User talk:Anne Delong|talk]]) 05:03, 6 April 2013 (UTC)}}

{{afc comment|1=The references listed are not [[WP:Reliable sources]].  Sources which are editable by members of the general public are generally not considered reliable. [[User:davidwr|davidwr]]/<small><small>([[User_talk:davidwr|talk]])/([[Special:Contributions/Davidwr|contribs]])/([[Special:Emailuser/davidwr|e-mail]])</small></small> 03:24, 21 April 2013 (UTC)}}
{{AFC submission|d|v|declinets=20130414084629|decliner=Wikignome|ts=20130406143016|u=71.94.235.210|ns=5|small=yes|small=yes|small=yes}}
----
<ref>http://wikimapia.org/27131620/Haiden-island</ref>
{{Coord|43|42|1.7|N|124|0|39|W|region:RU_type:city|display=title}}

== Haiden island <ref>http://www.geonames.org/8521402/haiden-island.html</ref> ==

Haiden island is the westernmost island in a four island chain (referred to as the "H" islands), found approximately 16 miles upriver from the mouth of the Umpqua River in Douglas County, Oregon USA. Island is has about 1100ft x 250ft (6.5 acres) at high tide and densely vegetated. Island features a small sandy beach area on it's eastern end which is suitable for reaching the island via small boat. Haiden island is a popular kayak destination during spring and summer months.

== References ==

{{Reflist}}

== External Links ==

*http://www.panoramio.com/photo/88305847
*
*
"""
        val submissions = SubmissionTemplate.getSubmissionData(pagecontent).toList
        assert(submissions.length == 3)    }
  }
  
  it("should be able to parse timestamps"){
    val formatted = "20130420034914"
    assert(SubmissionTemplate.parseDateTime(formatted) == Some(new DateTime(2013, 4, 20, 3, 49, 14, 0, DateTimeZone.UTC)))
  }
  

}