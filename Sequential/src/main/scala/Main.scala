import Futoshiki._



object FutoshikiSolverApp extends App {
  val grid: Array[Array[Option[Int]]] = Array(
    Array(None, None, None, Some(7), None, None, None, None, None),
    Array(None, Some(7), None, None, None, None, None, None, None),
    Array(Some(1), None, None, Some(2), None, None, None, None, Some(6)),
    Array(None, None, None, None, None, None, None, None, None),
    Array(None, None, None, None, None, Some(9), None, None, Some(7)),
    Array(None, None, None, Some(1), None, Some(5), None, None, None),
    Array(None, None, None, None, None, None, None, None, None),
    Array(Some(9), None, None, Some(3), None, None, None, None, None),
    Array(None, None, None, None, None, None, None, None, None)
  )

  val constraints = Seq(
    Constraint(Cell(0, 3), Cell(1, 3), '>'),
    Constraint(Cell(1, 3), Cell(1, 2), '>'),
    Constraint(Cell(1, 8), Cell(1, 7), '>'),
    Constraint(Cell(1, 7), Cell(1, 6), '>'),
    Constraint(Cell(1, 4), Cell(1, 5), '<'),
    Constraint(Cell(1, 5), Cell(2, 5), '>'),
    Constraint(Cell(2, 5), Cell(2, 6), '>'),
    Constraint(Cell(2, 6), Cell(3, 6), '<'),
    Constraint(Cell(2, 7), Cell(2, 8), '>'),
    Constraint(Cell(3, 1), Cell(3, 2), '>'),
    Constraint(Cell(3, 4), Cell(3, 3), '>'),
    Constraint(Cell(4, 3), Cell(4, 2), '<'),
    Constraint(Cell(4, 2), Cell(5, 2), '<'),
    Constraint(Cell(5, 0), Cell(5, 1), '>'),
    Constraint(Cell(6, 7), Cell(5, 7), '<'),
    Constraint(Cell(5, 7), Cell(5, 8), '>'),
    Constraint(Cell(5, 8), Cell(6, 8), '>'),
    Constraint(Cell(5, 1), Cell(6, 1), '>'),
    Constraint(Cell(6, 1), Cell(6, 2), '<'),
    Constraint(Cell(6, 4), Cell(6, 5), '<'),
    Constraint(Cell(6, 5), Cell(7, 5), '>'),
    Constraint(Cell(8, 6), Cell(8, 5), '<'),
    Constraint(Cell(8, 5), Cell(7, 5), '<'),
    Constraint(Cell(8, 7), Cell(8, 8), '>')
  )

  val solver = new FutoshikiSolver(grid, constraints)
  solver.initialize()

  if (solver.solve()) {
    println("Solution found:")
    solver.printGrid()
  } else {
    println("No solution found.")
  }
}
