package scaldingbot.net

import akka.actor.Actor
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scaldingbot.FreddyFileResque.FullFileInfo

class ImageInfoRequester extends Actor {
  import ExecutionContext.Implicits.global
  val downloader = context.actorSelection("../downloader")
  def receive = {
    case name : String => {
      val info : Future[FullFileInfo] = getfullfileinfo(name);
      for (fffi <- info) {
       downloader ! fffi
      }
    }
  }
  
  def getfullfileinfo(name : String): Future[FullFileInfo] = {
    ???
  }

}