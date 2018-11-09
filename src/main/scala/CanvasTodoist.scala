import com.softwaremill.macwire.wire

object CanvasTodoist {
  lazy private val _ = wire[HttpService]
  lazy private val canvasService = wire[CanvasService]
  lazy private val todoistService = wire[TodoistService]

  def main(args: Array[String]): Unit = {
    // TODO: Look into ways to differentiate between assignments and events
    // TODO: Setup sbt to have tasks for running main
    val assignments = Entries(todoistService).filterNew(canvasService.upcomingAssignments)
    assignments.foreach(todoistService.createTask)
  }
}
