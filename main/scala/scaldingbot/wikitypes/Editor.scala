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
  
  implicit def IPv4Address2IP4(address : IPv4Address) : IP4 = {
    IP4(address)
  }
    implicit def IPv6Address2IP6(address : IPv6Address) : IP6 = {
    IP6(address)
  }
  
  def fetchForName(username : String) : Editor = {
    IPv4Address.parseIp(username) match {
      case Some(ip) => ip
      case None => {
        IPv6Address.parseIp(username) match {
          case Some(ip) => ip
          case None => fetchRegisteredUser(username)
        }
      }
    }
  }
  
  def fetchRegisteredUser(username : String) : Editor = {
    Registered(0, username)
  }
}