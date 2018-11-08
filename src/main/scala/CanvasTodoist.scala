import com.softwaremill.macwire.wire

object CanvasTodoist {
  lazy private val _ = wire[HttpService]
  lazy private val canvasService = wire[CanvasService]
  lazy private val todoistService = wire[TodoistService]

  def main(args: Array[String]): Unit = {
    // TODO: Look into ways to differentiate between assignments and events
    // TODO: Difference between taskDate and dueDate should be proportional to how far in the future the assignment is
    // TODO: Refactor Task and assignment to be the same class
    val assignments = Entries.filterNew(canvasService.upcomingAssignments)
    assignments.foreach(todoistService.createTask)
  }
}
