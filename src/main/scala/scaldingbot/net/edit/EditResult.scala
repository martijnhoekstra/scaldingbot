package scaldingbot.net.edit

import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat
import spray.json._
import org.joda.time.DateTime


abstract class EditResult {
  val result : String
}

case class EditSuccess(pageid : Int, title : String, contentmodel : String, oldrevid : Int, newrevid : Int, newtimestamp : DateTime) extends EditResult {
  val result = "Success"
}

object EditResultJsonProtocol extends DefaultJsonProtocol {
  implicit object EditResultFormat extends RootJsonFormat[EditResult] {
   def write(b: EditResult) = {
     ???
   }
    def read(value: JsValue) = {
      val inner = value.asJsObject.fields.get("edit").get.asJsObject
      inner.getFields("result", "pageid", "title", "contentmodel", "oldrevid", "newrevid", "newtimestamp") match {
        case Seq(JsString("Success"), JsNumber(pageid), JsString(title), JsString(contentmodel), JsNumber(oldrevid),
                 JsNumber(newrevid), JsString(tsstring)) => {
                   EditSuccess(pageid.toInt, title, contentmodel, oldrevid.toInt, newrevid.toInt, new DateTime(tsstring))
        }
        case x => throw new DeserializationException("unrecognised edit format" + value)

      }
    }
  }
}