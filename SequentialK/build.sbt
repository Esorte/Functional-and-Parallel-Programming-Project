import sys.process._

val scala3Version = "3.4.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "SequentialK",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "io.circe" %% "circe-core" % "0.14.1",
      "io.circe" %% "circe-generic" % "0.14.1",
      "io.circe" %% "circe-parser" % "0.14.1"
    )
  )

// Define a task to run the Python script and wait for it to finish
val runPythonGUI = taskKey[Unit]("Runs the Python GUI for board input")

runPythonGUI := {
  val log = streams.value.log
  log.info("Running Python GUI...")
  // This command runs the Python script and waits for it to finish
  val pythonCommand = "python gui.py"
  val result = pythonCommand.!
  log.info(s"Python GUI exited with code $result")
}

// Define a new task that runs the Python GUI and then runs the main Scala application
val runp = taskKey[Unit]("Runs the Python GUI and then the Scala application")

// Corrected definition of runWithPythonGUI task
runp := Def.sequential(
  runPythonGUI,
  (Compile / run).toTask("")
).value