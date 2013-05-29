package ipaddress

trait IPAddress {

}

object IPAdress {
  def isValid(candidate : String) = {
    IPv4Address.isValid(candidate) || IPv6Address.isValid(candidate) 
  }
  
}