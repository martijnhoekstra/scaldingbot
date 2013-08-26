package scaldingbot.net

import scaldingbot.net.query.ApiPropertyValueSet

sealed abstract class Format extends ApiPropertyValueSet {
    val name = "format"
}

case object JSon extends Format {
  val values = Set("json")
}