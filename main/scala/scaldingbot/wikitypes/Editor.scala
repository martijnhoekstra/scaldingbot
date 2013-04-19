package scaldingbot.wikitypes

import ipaddress._

abstract class Editor;
abstract class IP extends Editor;
case class IP6 (address : IPv6Address) extends IP;
case class IP4 (address : IPv4Address) extends IP;
case class Registered (id : Long, name : String) extends Editor;

object Editor {
  var cache : Map[String, Editor] = Map.empty
  def fromName(username : String) = {
    if (!cache.contains(username)) {
      cache = cache + (username -> fetchForName(username))
    }
    cache(username)   
  }
  
  def fetchForName(username : String) = {
    ???
  }
}