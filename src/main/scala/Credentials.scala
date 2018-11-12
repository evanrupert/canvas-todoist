import play.api.libs.json.{JsValue, Json}

import scala.io.Source

object Credentials {
  private val CREDENTIALS_FILE_PATH =
    "./data/credentials.json"

  def canvasToken: String =
    sys.env.get("CANVAS_TOKEN") match {
      case Some(x) => x
      case None => credentialsFileJson("CANVAS_TOKEN").as[String]
    }

  def todoistToken: String =
    sys.env.get("TODOIST_TOKEN") match {
      case Some(x) => x
      case None => credentialsFileJson("TODOIST_TOKEN").as[String]
    }

  def credentialsFileJson: JsValue = {
    val source = Source.fromFile(CREDENTIALS_FILE_PATH)

    try Json.parse(source.mkString) finally source.close()
  }
}
