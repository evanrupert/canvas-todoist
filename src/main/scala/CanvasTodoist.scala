import java.util.Optional

import com.microsoft.azure.functions._
import com.softwaremill.macwire.wire
import com.microsoft.azure.functions.annotation._

object CanvasTodoist {
  lazy private val _ = wire[HttpService]
  lazy private val canvasService = wire[CanvasService]
  lazy private val todoistService = wire[TodoistService]

  @FunctionName("CanvasTodoist")
  def run(@TimerTrigger(name = "canvasTodoistTrigger", schedule = "0 0 * * *") timerInfo: String, context: ExecutionContext): Unit = {
    val assignments = Entries(todoistService).filterNew(canvasService.upcomingAssignments)
    assignments.foreach(todoistService.createTask)
  }

  @FunctionName("TestFunction")
  def testFunction(@HttpTrigger(name = "req", methods = Array(HttpMethod.GET, HttpMethod.POST), authLevel = AuthorizationLevel.ANONYMOUS)
          request: HttpRequestMessage[Optional[String]],
          context: ExecutionContext): HttpResponseMessage = {
    context.getLogger.info("Scala HTTP trigger processed a request.")
    val query: String = request.getQueryParameters.get("name")
    val name: String = request.getBody.orElse(query)
    if (name == null) {
      request
        .createResponseBuilder(HttpStatus.BAD_REQUEST)
        .body("Please pass a name on the query string or in the request body")
        .build()
    } else {
      request
        .createResponseBuilder(HttpStatus.OK)
        .body("Hello, " + name + "!")
        .build()
    }
  }
}
