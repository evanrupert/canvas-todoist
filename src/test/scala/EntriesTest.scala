import org.joda.time.DateTime
import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory

class EntriesTest extends FunSuite with MockFactory {
  private val today = new DateTime(2018, 11, 15, 0, 0, 0)

  test("filterNew filters out all entries that are already in todoist") {
    // TODO: Write test for filterNew
    // NOTE: since Entries is an object and not a class, Figure out how to inject the mock todoistService somehow
  }
}
