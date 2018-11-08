package models

import org.joda.time.DateTime
import play.api.libs.json.{JsObject, JsString}

class Entry(val content: String, val date: DateTime) {
  def dateWithinSevenDays: Boolean =
    date.isAfterNow && date.isBefore(DateTime.now.plusDays(7))

 def json: String =
    JsObject(Seq(
      "content" -> JsString(content),
      "date" -> JsString(date.toString("YYYY-MM-dd"))
    )).toString()
}
