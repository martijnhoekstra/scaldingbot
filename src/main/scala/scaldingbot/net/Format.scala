package scaldingbot.net

sealed abstract class Format extends ApiPropertyValueSet {
    val name = "format"
      
}

case object JSon extends Format {
  val values = Set("json")
}