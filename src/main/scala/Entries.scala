import models.Entry
import com.softwaremill.macwire.wire

object Entries {
  lazy private val _ = wire[HttpService]
  lazy private val todoistService = wire[TodoistService]

  def filterNew(entries: List[Entry]): List[Entry] = {
    val upcomingTodoistEntries =
      todoistService.tasks.filter(t => t.dateWithinSevenDays).map(t => t.content)

    entries.filter(e => !upcomingTodoistEntries.contains(e.content))
  }
}
