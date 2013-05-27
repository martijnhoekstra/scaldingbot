package test.ipaddress

import org.scalatest._
import ipaddress.IPv6Address

class IPv6Test extends FunSpec {
  describe("An IPv6 address") {
    it("should provide a BigInt power function") {
      assert(IPv6Address.intpow(2, 2) == 4)
      assert(IPv6Address.intpow(256, 0) == 1)
      assert(IPv6Address.intpow(256, 1) == 256)
      assert(IPv6Address.intpow(256, 2) == 65536)
    }
    
    it("should provide a parser for hex chars"){
      assert(IPv6Address.hexChar2Int('0') == Some(0))
      assert(IPv6Address.hexChar2Int('1') == Some(1))
      assert(IPv6Address.hexChar2Int('9') == Some(9))
      assert(IPv6Address.hexChar2Int('A') == Some(10))
      assert(IPv6Address.hexChar2Int('F') == Some(15))
      assert(IPv6Address.hexChar2Int('G') == None)
      assert(IPv6Address.hexChar2Int('-') == None)
    }

    it("should parse ipv6 groups") {
      assert(IPv6Address.parseGroup("0") == Some(0))
      assert(IPv6Address.parseGroup("1") == Some(1))
      assert(IPv6Address.parseGroup("01") == Some(1))
      assert(IPv6Address.parseGroup("000E") == Some(14))
      assert(IPv6Address.parseGroup("FFFF") == Some(65535))

    }

    it("should not parse ill formed ipaddress") {
      assert(IPv6Address.parseGroup("GGGG") == None)
      assert(IPv6Address.parseGroup("12345") == None)
    }

   
    it("should provide a function that parses full addresses") {
      assert(IPv6Address.toBigInt("0:0:0:0:0:0:0:FFFF") == Some(65535))
      assert(IPv6Address.toBigInt("0:0:0:0:0:0:1:1") == Some(65537))

      
    }
    
     it("should be able to parse collapsed 0's (::)") {
      assert(IPv6Address.toBigInt("0:0:0:0:1::1") == Some(281474976710657L))
    }

    
    it("should be able to parse collapsed 0's (::) in lead position") {
      assert(IPv6Address.toBigInt("::FFFF") == Some(65535))
      assert(IPv6Address.toBigInt("::1:1") == Some(65537))
    }

  }
}