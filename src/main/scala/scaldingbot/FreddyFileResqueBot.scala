package scaldingbot

import scaldingbot.FreddyFileResque._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scalaz._, Scalaz._, scalaz.contrib.std._

object FreddyFileResqueBot {
  val catInfo = Cats.getCatInfo
  val catFileInfo = catInfo.map(getFileInfo)
  catFileInfo.map(processFiles)

  def getFileInfo(catInfo: CatInfo) = {
    val filenames = getFileNames(catInfo.catname)
    filenames.map(filename => (filename, catInfo))
  }

  def getFileNames(catName: String): Stream[String] = {
    ???
  }

  def processFiles(info: Stream[(String, CatInfo)]) = {
    import ExecutionContext.Implicits.global

    val ffullFilesInfo = Future.traverse(info)(getFullInfo)
    //info.map(getFullInfo)

    for (fullFileInfo <- ffullFilesInfo) {
      val fdownloads = Future.traverse(fullFileInfo)(downloadFile)
      for (downloads <- fdownloads) {
        val fuploads = Future.traverse(downloads)(upload)
        for (uploads <- fuploads) {
          uploads map addDeletionCat
        }
      }
    }
  }

  def getFullInfo(info: (String, CatInfo)): Future[FullFileInfo] = {
    ???
  }

  def downloadFile(fileInfo: FullFileInfo): Future[FullFileInfo] = {
    ???
  }

  def upload(ffi: FullFileInfo): Future[FullFileInfo] = {
    ???
  }

  def addDeletionCat(ffi: FullFileInfo) {
    ???
  }

}