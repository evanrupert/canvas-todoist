import models.Assignment
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

class CanvasService(httpService: HttpService) {
  private val BASE_URL = "https://webcourses.ucf.edu/"

  def upcomingAssignments: List[Assignment] = parseJSON(requestUpcomingEvents)

  private def requestUpcomingEvents: String =
    httpService.request(
      BASE_URL + "api/v1/users/self/upcoming_events",
      Seq(("Authorization", "Bearer " + Credentials.canvasToken))
    )

  private def parseJSON(body: String): List[Assignment] = {
    val json = Json.parse(body)

    val pattern = "yyyy-MM-dd'T'HH:mm:ssZ"
    implicit val datetimeReads: Format[DateTime] =
      Format[DateTime](JodaReads.jodaDateReads(pattern), JodaWrites.jodaDateWrites(pattern))

    case class AssignmentInput(title: String, dueDate: DateTime)

    implicit val assignmentReads: Reads[AssignmentInput] = (
      (JsPath \ "title").read[String] and
      (JsPath \ "assignment" \ "due_at").read[DateTime]
    )(AssignmentInput.apply _)

    json.as[List[AssignmentInput]].map(a => new Assignment(a.title, a.dueDate))
  }
}
