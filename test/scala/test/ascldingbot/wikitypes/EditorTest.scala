package test.ascldingbot.wikitypes
import org.scalatest._
import scaldingbot.wikitypes._

class EditorTest extends FunSpec {
  describe("An Editor") {
    it("should be able to parse IPv4 anons") {
      Editor.fromName("192.168.0.1") match {
        case editor : IP4 => assert(true)
        case _ => assert(false)
        }
    }
    
    it("should be able to parse IPv6 anons") {
      Editor.fromName("::1") match {
        case editor : IP6 => assert(true)
        case _ => assert(false)
        }
    }
    
    it("should be able to parse named editors") {
      Editor.fromName("Henk") match {
        case editor : Registered => {
          assert(editor.name == "Henk")
        }
        case _ => assert(false)
      }
    }
    
    
  }

}