package scaldingbot.net

import scala.concurrent.ExecutionContext
import akka.actor.Actor
import scaldingbot.FreddyFileResque.FullFileInfo
import spray.client.pipelining._
import spray.util._
import scaldingbot.FreddyFileResque.FileDownloadInfo
import spray.http.HttpResponse
import scala.util._
import spray.http.HttpBody
import java.io.FileOutputStream
import akka.event.Logging

class ImageDownloader extends Actor {
  implicit val dispatch = context.dispatcher;
  val log = Logging(context.system, this)
  val uploader = context.actorSelection("../ImageUploader")
  def receive = {
    case info: FileDownloadInfo => {
      val filepipeline = sendReceive
      val descpipeline = sendReceive
      val fileresfut = filepipeline {
        Get(info.url);
      }
      val descresfut = descpipeline {
        Get(info.descurl)
      }

      val smt = for {
        fileres <- fileresfut
        descres <- descresfut
      } yield (getbytes(fileres), getdesc(descres))

      for (data <- smt){
        data._1 match {
          case Some(bytes) => {
            uploader ! data._2.copy(content = data._1)
          }
        }
      }
    }

  }
  
  def getdesc(res : HttpResponse) : FullFileInfo = {
    ???
  }
  
  def getbytes(res : HttpResponse) = {
    for {body <- res.entity.toOption} yield body.buffer
  }

}