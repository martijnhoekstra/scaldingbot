package scaldingbot.wikitypes

import ipaddress._

abstract class Editor;
abstract class IP extends Editor;
case class IP6 (address : IPv6Address) extends IP;
case class IP4 (address : IPv4Address) extends IP;
case class Registered (id : Long, name : String) extends Editor;

