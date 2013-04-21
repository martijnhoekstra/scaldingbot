package ipaddress

import scala.runtime.RichChar

class IPv6Address(address: BigInt) extends IPAddress {

  def getValue(ipstring: String) = {

    def hex2bytes(hex: String): List[Byte] = {
      hex.length() match {
        case 1 => 0.toByte :: hex.toByte :: Nil
        case 2 => 0.toByte :: hex.toByte :: Nil
        case 3 => hex.substring(0, 0).toByte :: hex.substring(1, 2).toByte :: Nil
        case 4 => hex.substring(0, 1).toByte :: hex.substring(2, 3).toByte :: Nil
      }
    }

    val highstring :: lowstring :: none = ipstring.split("::").toList
    if (none != Nil) throw new IllegalArgumentException(s"ipstring $ipstring was not a legal IPv6 address")
    val highbytes = highstring.split(':').toList.map(hex2bytes).flatten
    val lowbytes = lowstring.split(':').toList.map(hex2bytes).flatten
    val highval = highbytes.zip(List.range(16, 0, -1)).foldLeft(BigInt(0))((s, i) => s + i._1 * i._2)
    val lowval = lowbytes.reverse.zip(List.range(0, 16, 1)).foldLeft(BigInt(0))((s, i) => s + i._1 * i._2)
    highval + lowval

  }
}

object IPv6Address {
  def intpow(base: Long, exp: Int) = {
    (0 until exp).foldLeft(BigInt(1))((tot, _) => tot * base)
  }

  def hexChar2Int(char: Char) = {
    if (char >= '0' && char <= '9') Some(char.byteValue - (new RichChar('0').byteValue))
    else if (char >= 'A' && char <= 'F') {
      Some(10 + char.byteValue - (new RichChar('A').byteValue))
    } else None
  }

  def parseGroup(group: String) = {
    if (group.length() > 4) None
    else {
       val ints = group.map(hexChar2Int).flatten.reverse
       if(group.length() != ints.count(_ => true)) None
       else {
         val zipped = ints.zip(0 until 5)
         val multiplied = zipped.map(t => t._1 * intpow(16, t._2))
         Some(multiplied.foldLeft(0 : BigInt)((sum, next) => sum + next))
       }
    }
  }

  def parseIp(address: String) = {
    toBigInt(address) match {
      case Some(ip) => Some(new IPv6Address(ip))
      case None => None
    }
  }

  def toBigInt(address: String): Option[BigInt] = {
    val splitted = address.split("""::""").toList
    val split = splitted match {
      case head :: second :: more :: Nil => Nil
      case head :: second :: Nil => splitted
      case head :: Nil => head :: "" :: Nil
      case _ => Nil
    }
    split match {
      case Nil => None
      case highstring :: lowstring :: Nil => {
        val highgroups = highstring.split(':').map(parseGroup)
        val lowgroups = lowstring.split(':').map(parseGroup)
        if (!highgroups.forall(_.isDefined)) None
        else if (!lowgroups.forall(_.isDefined)) None
        else {
          val zipped = highgroups.toList.flatten.zip(7 until (-1, -1)) ::: lowgroups.toList.flatten.reverse.zip(0 until 8)
          val sum = zipped.map(t => t._1 * intpow(65536, t._2)).foldLeft(BigInt(0))(_ + _)
          Some(sum)
        }
      }
    }
  }
}