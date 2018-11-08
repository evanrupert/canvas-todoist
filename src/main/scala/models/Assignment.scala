package models

import org.joda.time.{DateTime, Days}
import play.api.libs.json.{JsObject, JsString}

class Assignment(title: String, dueDate: DateTime) extends Entry(title, dueDate) {
  private val DATE_PATTERN = "YYYY-MM-dd"

  def workDate: DateTime = {
    val daysUntilDue = Days.daysBetween(DateTime.now, date).getDays

    val daysBeforeDueDate: Int = Math.ceil(daysUntilDue / 2).asInstanceOf[Int]

    date.minusDays(daysBeforeDueDate)
  }

  override def json: String = {
    JsObject(Seq(
      "content" -> JsString(title),
      "due_date" -> JsString(workDate.toString(DATE_PATTERN))
    )).toString()
  }
}
