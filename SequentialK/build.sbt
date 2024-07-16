val scala3Version = "3.4.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Parallel",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
  )

// Define a task to run the Python script
val runPythonGUI = taskKey[Unit]("Runs the Python GUI for board input")

runPythonGUI := {
  // Use 'sys.process' to run the Python script
  val log = streams.value.log
  log.info("Running Python GUI...")
  val pythonCommand = "python gui.py"
  pythonCommand.!
}

// Make 'run' depend on 'runPythonGUI', so it runs the Python script first
run := (run dependsOn runPythonGUI).evaluated