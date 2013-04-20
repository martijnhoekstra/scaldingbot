package test

import org.scalatest._


class Universe extends FunSpec {
  describe("The Universe") {
    it ("should respect addition"){
      assert(1 + 1 == 2)
    }
  
    it ("should respect the law of contradiction"){
      val a = false
      val b = false
      if (a && !a){
        assert(a == true)
      } else {
        assert(a == false)
      }
    }
  }
}