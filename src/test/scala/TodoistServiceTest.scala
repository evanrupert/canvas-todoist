import models.Assignment
import org.joda.time.{DateTime, DateTimeUtils}
import org.scalatest.FunSuite
import org.scalamock.scalatest.MockFactory

class TodoistServiceTest extends FunSuite with MockFactory {
  private val tasksGetJson =
    """
      | [
      |   {
      |    "content": "Walk Dogs",
      |    "completed": false,
      |    "label_ids": [],
      |    "order": 11,
      |    "indent": 1,
      |    "priority": 1,
      |    "comment_count": 0,
      |    "due": {
      |      "recurring": true,
      |      "string": "ev thur",
      |      "date": "2018-11-08"
      |    }
      |  },
      |  {
      |    "content": "Feed Cats",
      |    "completed": false,
      |    "label_ids": [],
      |    "order": 12,
      |    "indent": 1,
      |    "priority": 1,
      |    "comment_count": 0,
      |    "due": {
      |      "recurring": true,
      |      "string": "ev 20th",
      |      "date": "2018-11-20"
      |    }
      |  }
      | ]
    """.stripMargin

  private val today = new DateTime(2018, 11, 15, 0, 0, 0)

  test("todoistService properly parses tasks json into tasks list") {
    val httpServiceStub = stub[HttpService]
    val todoistService = new TodoistService(httpServiceStub)

    (httpServiceStub.request _)
      .when("https://beta.todoist.com/API/v8/tasks", Seq(("Authorization", "Bearer " + Credentials.todoistToken)))
      .returns(tasksGetJson)

    val results = todoistService.tasks

    assert(results.head.content.equals("Walk Dogs"))
    assert(results.head.date.equals(new DateTime(2018, 11, 8, 0, 0, 0)))

    assert(results(1).content.equals("Feed Cats"))
    assert(results(1).date.equals(new DateTime(2018, 11, 20, 0, 0, 0)))
  }

  test("todoistService properly sends POST request to create new task") {
    val httpServiceMock = mock[HttpService]
    val todoistService = new TodoistService(httpServiceMock)

    (httpServiceMock.postRequest _).expects(
      "https://beta.todoist.com/API/v8/tasks",
      Seq(("Authorization", "Bearer " + Credentials.todoistToken), ("Content-Type", "application/json")),
      """{"content":"Feed Cats","due_date":"2018-11-18"}"""
    )

    DateTimeUtils.setCurrentMillisFixed(today.getMillis)
    todoistService.createTask(new Assignment("Feed Cats", new DateTime(2018, 11, 20, 0, 0, 0)))
  }
}
