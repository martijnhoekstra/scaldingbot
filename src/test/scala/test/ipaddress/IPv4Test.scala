package test.ipaddress

import org.scalatest._
import ipaddress.IPv4Address

class IPv4Test extends FunSpec {
  describe("An IPv4 address") {
    it("shoul dprovide an integer power function") {
      assert(IPv4Address.intpow(2, 2) == 4)
      assert(IPv4Address.intpow(256, 0) == 1)
      assert(IPv4Address.intpow(256, 1) == 256)
      assert(IPv4Address.intpow(256, 2) == 65536)
    }

    it("should parse octets") {
      assert(IPv4Address.parseOctet("0") == Some(0))
      assert(IPv4Address.parseOctet("1") == Some(1))
      assert(IPv4Address.parseOctet("255") == Some(255))
      assert(IPv4Address.parseOctet("-1") == None)
      assert(IPv4Address.parseOctet("256") == None)
    }

    it("should not parse ill formed ipaddress") {
      assert(IPv4Address.toInt("0.0.0.0.0") == None)
      assert(IPv4Address.toInt("0.0") == None)
      assert(IPv4Address.toInt("a.b.c.d") == None)
    }

    it("should parse final octets") {
      assert(IPv4Address.toInt("0.0.0.0") == Some(0))
      assert(IPv4Address.toInt("0.0.0.1") == Some(1))
    }

    it("should provide a function to convert an IPv4 string to an integer") {
      assert(IPv4Address.toInt("0.0.1.0") == Some(256))
      assert(IPv4Address.toInt("0.0.1.2") == Some(258))
    }
    
    it("should be able to format an IP address"){
      assert(IPv4Address.parseIp("0.0.0.0").get.toString == "0.0.0.0")
      assert(IPv4Address.parseIp("0.1.2.3").get.toString == "0.1.2.3")
      assert(IPv4Address.parseIp("255.0.255.1").get.toString == "255.0.255.1")
    }

  }
}