package models

import org.joda.time.DateTime

class Task(content: String, dueDate: DateTime) extends Entry(content, dueDate)
