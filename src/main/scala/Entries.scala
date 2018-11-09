import models.Entry

class Entries(todoistService: TodoistService) {
  def filterNew(entries: List[Entry]): List[Entry] = {
    val upcomingTodoistEntries =
      todoistService.tasks.filter(t => t.dateWithinSevenDays).map(t => t.content)

    entries.filter(e => !upcomingTodoistEntries.contains(e.content))
  }
}

object Entries {
  def apply(todoistService: TodoistService): Entries = new Entries(todoistService)
}
