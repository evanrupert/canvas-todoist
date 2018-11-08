import models.Assignment
import org.joda.time.DateTime
import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory
class CanvasServiceTest extends FunSuite with MockFactory {
  private val json =
    """
      | [
      |   {
      |     "title": "Math Chapter 3",
      |     "other_field": "unused value",
      |     "assignment": {
      |       "due_at": "2018-11-13T04:59:00Z",
      |       "another_field": "unused value"
      |     }
      |   }
      | ]
    """.stripMargin

  private val expectedDate = new DateTime(2018, 11, 12, 23, 59, 0)
  private val expectedAssignments = List(new Assignment("Math Chapter 3", expectedDate))

  test("Canvas.upcomingAssignments should properly parse the json and return assignment instancees") {
    val httpServiceStub = stub[HttpService]

    val canvasService = new CanvasService(httpServiceStub)

    (httpServiceStub.request _)
      .when("https://webcourses.ucf.edu/api/v1/users/self/upcoming_events",
            Seq(("Authorization", "Bearer " + Credentials.canvasToken)))
      .returns(json)

    val results = canvasService.upcomingAssignments

    println(results.head.date)

    assert(expectedAssignments.head.content.equals(results.head.content))
    assert(expectedAssignments.head.date.equals(results.head.date))
  }
}
