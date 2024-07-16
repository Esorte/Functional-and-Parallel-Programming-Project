package sequentialf
import scala.io.Source
// import play.api.libs.json._

object Main extends App {
  // // Read the JSON file
  // val source = Source.fromFile("puzzle_data.json")
  // val jsonString = source.getLines.mkString
  // source.close()

  // // Parse JSON
  // val json = Json.parse(jsonString)
  
  // val boardSize = (json \ "board_size").as[Int]
  // val initialDigits = (json \ "initial_digits").as[Map[(Int, Int), Int]]
  // val constraints = (json \ "constraints").as[List[((Int, Int), (Int, Int))]]
  
  // // Initialize your puzzle with these values
  // val puzzle = new FutoshikiPuzzle(boardSize, initialDigits, constraints)

  // val initialDigits = Map(
  //   (0, 1) -> 2, (0, 2) -> 3,
  //   (3, 1) -> 4, (3, 2) -> 2
  // )
  // val constraints = List(
  //   ((1, 1), (2, 1)),
  //   ((1, 0), (0, 0)),
  //   ((3, 2), (3, 3)),
  //   ((3, 2), (3, 1))

  // )

  // val puzzle = new FutoshikiPuzzle(4, initialDigits, constraints)
  // println("Initial Puzzle:")
  // println(puzzle)

  val initialDigits = Map(
    (0, 3) -> 7,
    (1, 1) -> 7,
    (2, 0) -> 1, (2, 3) -> 2, (2, 8) -> 6,
    (4, 5) -> 9, (4, 8) -> 7,
    (5, 3) -> 1, (5, 5) -> 5,
    (7, 0) -> 9, (7, 3) -> 3
  )

  val constraints = List(
    ((0, 3), (1, 3)), // >
    ((1, 3), (1, 2)), // >
    ((1, 8), (1, 7)), // >
    ((1, 7), (1, 6)), // >
    ((1, 4), (1, 5)), // <
    ((1, 5), (2, 5)), // >
    ((2, 5), (2, 6)), // >
    ((2, 6), (3, 6)), // <
    ((2, 7), (2, 8)), // >
    ((3, 1), (3, 2)), // >
    ((3, 4), (3, 3)), // >
    ((4, 3), (4, 2)), // <
    ((4, 2), (5, 2)), // <
    ((5, 0), (5, 1)), // >
    ((6, 7), (5, 7)), // <
    ((5, 7), (5, 8)), // >
    ((5, 8), (6, 8)), // >
    ((5, 1), (6, 1)), // >
    ((6, 1), (6, 2)), // <
    ((6, 4), (6, 5)), // <
    ((6, 5), (7, 5)), // >
    ((8, 6), (8, 5)), // <
    ((8, 5), (7, 5)), // <
    ((8, 7), (8, 8))  // >
  )


    val puzzle = new FutoshikiPuzzle(9, initialDigits, constraints)
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