import models.Task
import org.joda.time.{DateTime, DateTimeUtils}
import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory

class EntriesTest extends FunSuite with MockFactory {
  private val today = new DateTime(2018, 11, 15, 0, 0, 0)

  test("filterNew should filter out all duplicate assignments within the next seven days") {
    val assignmentsToBeAdded = List(
      new Task("Duplicate Task", today.plusDays(5)),
      new Task("Duplicate Future Task", today.plusDays(7)),
      new Task("Today Task", today),
      new Task("Duplicate Past Task", today.minusDays(1))
    )

    val entries = Entries(new TodoistService(new HttpService()) {
      override def tasks: List[Task] = List(
        new Task("Duplicate Task", today.plusDays(2)),
        new Task("Duplicate Future Task", today.plusDays(10)),
        new Task("Today Task", today),
        new Task("Duplicate Past Task", today.minusDays(1))
      )
    })

    DateTimeUtils.setCurrentMillisFixed(today.getMillis)
    val results = entries.filterNew(assignmentsToBeAdded).map { t => t.content }

    assert(results.size == 2)
    assert(results.head == "Duplicate Future Task")
    assert(results(1) == "Duplicate Past Task")
  }
}
