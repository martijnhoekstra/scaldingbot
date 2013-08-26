package scaldingbot.FreddyFileResque

case class FullFileInfo(filename : String, cat : String, source : String, author : String, content : Option[Array[Byte]])