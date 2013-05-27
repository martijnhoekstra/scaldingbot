package scaldingbot.net.query

import scala.collection.immutable.Set
import scala.collection.mutable.HashSet

class Info(val switches : scala.collection.immutable.Set[String], token : Option[TokenType], continue : Option[String]) extends PropertyGroup{
  val prefix = "in"
  val name = "info"

  def queryPairs : List[(String, String)] =
    switchpair ::
    token.map(t => ("token" , t.name) ).toList :::
    continue.map(c => ("continue" , c )).toList :::
    Nil
  
}


class InfoBuilder {
  val switches = new HashSet[String]()
  var token : Option[TokenType] = None
  var continue = ""
  def build = {
    val contOption = if (continue == "" ) None else Some(continue)
    new Info(switches.toSet, token, contOption)
  }
}

abstract class Inprop {
  val name : String
}

case class Protection() extends Inprop { override val name = "protection"}
case class Talkid() extends Inprop { override val name = "talkid" }
case class Subjectid() extends Inprop { override val name = "subjectid"}
case class Url() extends Inprop {override val name = "url"}
case class Preload() extends Inprop {override val name = "preload"}
case class Watched() extends Inprop {override val name = "watched" }

abstract class TokenType {
  val name : String
}
case class Edit() extends TokenType { override val name = "edit" }
case class Move() extends TokenType { override val name = "move" }
case class Delete() extends TokenType {override val name = "delete" }

