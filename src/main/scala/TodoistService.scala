import models.{Assignment, Entry, Task}
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalaj.http.Http

class TodoistService(httpService: HttpService) {
  private val BASE_URL = "https://beta.todoist.com/API/v8/"

  def tasks: List[Task] = parseTasksJson(requestAllTasks)

  def createTask(entry: Entry): String =
    Http(BASE_URL + "tasks")
      .headers(Seq(("Authorization", "Bearer " + Credentials.todoistToken),
                   ("Content-Type", "application/json")))
      .postData(entry.json)
      .asString
      .body

  private def requestAllTasks: String =
    Http(BASE_URL + "tasks")
      .headers(Seq(("Authorization", "Bearer " + token)))
      .asString
      .body

  private def parseTasksJson(body: String): List[Task] = {
    val json = Json.parse(body)

    val datePattern = "YYYY-MM-dd"
    implicit val datetimeReads: Format[DateTime] =
      Format[DateTime](JodaReads.jodaDateReads(datePattern), JodaWrites.jodaDateWrites(datePattern))

    case class TaskInput(content: String, dueDate: Option[DateTime])

    implicit val taskReads: Reads[TaskInput] = (
      (JsPath \ "content").read[String] and
      (JsPath \\ "date").readNullable[DateTime]
    )(TaskInput.apply _)

    json.as[List[TaskInput]].foldRight(List.empty[Task])((t, acc) => t.dueDate match {
      case Some(d) => new Task(t.content, d) :: acc
      case None => acc
    })
  }

  private def token: String =
    sys.env.get("TODOIST_TOKEN") match {
      case Some(v) => v
      case None => throw new Exception("No Todoist Token found in Environment Variables")
    }
}