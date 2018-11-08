name := "canvas_todoist"

version := "0.1"

scalaVersion := "2.12.7"

// HTTP library
libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.1"

// DateTime library
libraryDependencies += "joda-time" % "joda-time" % "2.10.1"

// JSON Parsing libraries
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.10"
libraryDependencies += "com.typesafe.play" %% "play-json-joda" % "2.6.0-RC1"

// Testing Frameworks
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % Test

// Dependency Injection Frameworks
libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.3.1" % "provided"
libraryDependencies += "com.softwaremill.macwire" %% "macrosakka" % "2.3.1" % "provided"
libraryDependencies += "com.softwaremill.macwire" %% "util" % "2.3.1"
libraryDependencies += "com.softwaremill.macwire" %% "proxy" % "2.3.1"