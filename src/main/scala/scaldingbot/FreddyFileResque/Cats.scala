package scaldingbot.FreddyFileResque

case class CatInfo(catname : String, source : String, author : String)

object Cats {
  lazy val getCatInfo = CatInfo("Diagrams of Australian national highway markers",
                                "AS1743 (AS1743-2001 Pages 202-207), Colours approximated.",
                                "[[Standards Australia]]<br />'''This image:''' $commonsauthor") ::
                        CatInfo("Diagrams of Australian national route markers",
                                "AS1743 (AS1743-2001 Pages 202-207), Colours approximated.",
                                "[[Standards Australia]]<br />'''This image:''' $commonsauthor") ::
                        CatInfo("Diagrams of Australian state route markers",
                                "AS1743 (AS1743-2001 Pages 202-207), Colours approximated.",
                                "[[Standards Australia]]<br />'''This image:''' $commonsauthor") ::
                        CatInfo("Diagrams of Australian alphanumeric route markers",
                                "Manual of Uniform Traffic Control Devices, 2003 Edition (Third Issue: 2nd April, 2012), [[Government of Queensland]], Page 56-57; with design approximated from many publicly available images ([http://www.ozroads.com.au/TAS/routenumbering/current/B72/38.jpg example] and [http://mrv.ozroads.com.au/SRNS/IMG_1675.JPG example]), Colours approximated.",
                                "[[Department of Main Roads (Queensland)]]<br />'''This image:''' $commonsauthor") ::
                        CatInfo("Diagrams of New South Wales alphanumeric route markers",
                                "Design approximated from many publicly available images (This set based largely on signage plans [http://www.ozroads.com.au/NSW/Special/MAB/289.jpg here] and [http://www.ozroads.com.au/NSW/Special/MAB/285.jpg here], as well as other imagery of these signs ([http://www.ozroads.com.au/NSW/Special/MAB/388.jpg example]), Colours approximated.",
                                "[[Roads and Traffic Authority]]<br />'''This image:''' $commonsauthor") ::
                        CatInfo("ACT Tourist Drive shields",
                                "Design approximated from many publicly available images ([http://expressway.paulrands.com/gallery/roads/act/numbered/touristdrives/td4/03_gungahlin/clockwise/images/200912_41_crace_gungahlindr.jpg example] and [http://www.ozroads.com.au/ACT/TD5/mainpic.JPG example]), Colours approximated.",
                                "[[ACT Territory and Municipal Services Directorate]]<br />'''This image:''' $commonsauthor") ::
                        CatInfo("Diagrams of Australian Metroad markers",
                                "Guide Sign \"Metroad Route Marker\" Diagram [http://www.tmr.qld.gov.au/~/media/busind/techstdpubs/TC%20signs/Compressed/tc9473tc9661pt2.pdf See page 37]; Colours approximated.",
                                "[[Department of Main Roads (Queensland)]]<br />'''This image:''' $commonsauthor") ::
                        Nil
}
                    