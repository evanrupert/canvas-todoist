import models.Task
import org.joda.time.{DateTime, DateTimeUtils}
import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory

class EntriesTest extends FunSuite with MockFactory {
  private val today = new DateTime(2018, 11, 15, 0, 0, 0)

  test("filterNew filters out all entries that are already in todoist") {
    val assignmentsToBeAdded = List(
      new Task("Walk Dogs", today.plusDays(5)),
      new Task("Clean House", today.plusDays(7))
    )

    val entries = Entries(new TodoistService(new HttpService()) {
      override def tasks: List[Task] = List(
        new Task("Walk Dogs", today.plusDays(2)),
        new Task("Feed Cats", today.plusDays(3)),
        new Task("Clean House", today.plusDays(10))
      )
    })

    DateTimeUtils.setCurrentMillisFixed(today.getMillis)
    val results = entries.filterNew(assignmentsToBeAdded).map { t => t.content }

    assert(results.size == 1)
    assert(results.head == "Clean House")
  }
}
