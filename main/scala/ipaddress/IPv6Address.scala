package ipaddress

class IPv6Address (ipstring : String) extends IPAddress {
  lazy val value = getValue(ipstring)
  
  def getValue(ipstring : String) = {
    
    def hex2bytes(hex : String) :List[Byte] = {
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
    val highval = highbytes.zip(List.range(16, 0, -1)).foldLeft(BigInt(0))((s, i) => s + i._1 * i._2 )
    val lowval = lowbytes.reverse.zip(List.range(0,16,1)).foldLeft(BigInt(0))((s, i) => s + i._1 * i._2)
    highval + lowval
    
  }
    
}