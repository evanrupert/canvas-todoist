import com.softwaremill.macwire.wire

object CanvasTodoist {
  lazy private val _ = wire[HttpService]
  lazy private val canvasService = wire[CanvasService]
  lazy private val todoistService = wire[TodoistService]

  def main(args: Array[String]): Unit = {
    // TODO: Look into ways to differentiate between assignments and events
    // TODO: Write additional test suites
    // TODO: Setup sbt to have tasks for running main and running tests
    val assignments = Entries.filterNew(canvasService.upcomingAssignments)
    assignments.foreach(todoistService.createTask)
  }
}
