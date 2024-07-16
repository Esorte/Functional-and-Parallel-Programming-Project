package sequentialf

object Main extends App {
  val initialDigits = Map(
    (0, 1) -> 2, (0, 2) -> 3,
    (3, 1) -> 4, (3, 2) -> 2
  )
  val constraints = List(
    ((1, 1), (2, 1)),
    ((1, 0), (0, 0)),
    ((3, 2), (3, 3)),
    ((3, 2), (3, 1))

  )

  val puzzle = new FutoshikiPuzzle(4, initialDigits, constraints)
  println("Initial Puzzle:")
  println(puzzle)

  PerformanceTest.perstart()
  Solver.solve(puzzle) match {
    case Some(solvedBoard) =>
      PerformanceTest.perend()
      println("Solved Puzzle:")
      for (row <- solvedBoard) {
        println(row.mkString(" "))
      }
    case None => 
      PerformanceTest.perend()
      println("No solution found.")
  }
}