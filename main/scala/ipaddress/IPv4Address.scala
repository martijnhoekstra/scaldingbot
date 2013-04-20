package ipaddress
import scala.math

class IPv4Address(address: Long) extends IPAddress {

}

object IPv4Address {
  def isIpv4Address(address: String) = {
    toInt(address) match {
      case None => false
      case _ => true
    }
  }

  def intpow(base: Long, exp: Int) = {
    (0 until exp).foldLeft(1L)((tot, _) => tot * base)
  }

  def parseOctet(octet: String): Option[Int] = {
    try {
      val asInt = octet.toInt
      if ((asInt >= 0) && (asInt <= 255)) {
        Some(asInt)
      } else {
        None
      }
    } catch {
      case _: Throwable => None
    }
  }

  def toInt(address: String) = {
    val octets = address.split("""\.""")
    if (octets.length != 4) None
    else {
      val intoctets = (octets map parseOctet).toList.flatten
      if (intoctets.length != 4) None
      else {
        val withOnum = intoctets.reverse.zip(0 until (5))
        Some(withOnum.map(t => t._1 * intpow(256, t._2)).sum)
      }
    }
  }
  
  def parseIp(address: String) = {
    toInt(address) match {
      case Some(ip) => Some(new IPv4Address(ip))
      case None => None
    }
  }
  
}