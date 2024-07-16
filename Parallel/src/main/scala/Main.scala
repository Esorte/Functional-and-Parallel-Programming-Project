package parf
import scala.io.Source
import io.circe._, io.circe.parser._, io.circe.generic.auto._

object Main extends App {
  // Custom KeyDecoder for (Int, Int) tuples
implicit val decodeMapKey: KeyDecoder[(Int, Int)] = new KeyDecoder[(Int, Int)] {
  override def apply(key: String): Option[(Int, Int)] = {
    key.trim.drop(1).dropRight(1).split(",").map(_.trim.toIntOption) match {
      case Array(Some(x), Some(y)) => Some((x, y))
      case _ => None
    }
  }
}

  // Read the JSON file
  val source = Source.fromFile("puzzle_data.json")
  val jsonString = source.getLines.mkString
  source.close()

  // Parse JSON
  val parsedJson = parse(jsonString).getOrElse(Json.Null)

  // Extract values
  val boardSize = parsedJson.hcursor.downField("board_size").as[Int].getOrElse(0)
  val initialDigits = parsedJson.hcursor.downField("initial_digits").as[Map[(Int, Int), Int]].getOrElse(Map.empty)
  val constraints = parsedJson.hcursor.downField("constraints").as[List[List[List[Int]]]].getOrElse(List.empty)

  println(initialDigits)

  val transformedConstraints = constraints.map {
    case List(List(x1, y1), List(x2, y2)) => ((x1, y1), (x2, y2))
    case _ => throw new IllegalArgumentException("Invalid constraint format")
  }
  
  // Initialize your puzzle with these values
  val puzzle = new FutoshikiPuzzle(boardSize, initialDigits, transformedConstraints)

  // val initialDigits = Map(
  //   (0, 1) -> 2, (0, 2) -> 3,
  //   (3, 1) -> 4, (3, 2) -> 2
  // )
  // val constraints = List(
  //   ((1, 1), (2, 1)),
  //   ((3, 2), (3, 3))
  // )

  // val puzzle = new FutoshikiPuzzle(4, initialDigits, constraints)
  // println("Initial Puzzle:")
  // println(puzzle)

  //*****************************************\\

  // val initialDigits = Map(
  //   (0, 0) -> 9, (0, 3) -> 3, (0, 5) -> 8,
  //   (1, 0) -> 7, (1, 3) -> 1, (1, 7) -> 8,
  //   (2, 1) -> 3, (2, 2) -> 2, (2, 4) -> 4, (2, 7) -> 1,
  //   (3, 1) -> 6, (3, 2) -> 9, (3, 8) -> 4,
  //   (4, 0) -> 4, (4, 4) -> 2, (4, 6) -> 5,
  //   (5, 0) -> 9, (5, 1) -> 1, (5, 2) -> 3, (5, 5) -> 6,
  //   (6, 1) -> 5, (6, 3) -> 7, (6, 6) -> 1, (6, 7) -> 3,
  //   (7, 0) -> 7, (7, 1) -> 4, (7, 4) -> 2,
  //   (8, 6) -> 8
  // )

  // val constraints = List(
  //   ((0, 1), (1, 1)),
  //   ((1, 4), (2, 4)),
  //   ((2, 0), (2, 1)), ((2, 1), (2, 2)), ((2, 2), (2, 3)), ((2, 4), (2, 5)),
  //   ((3, 1), (3, 2)), ((3, 2), (3, 3)), ((3, 4), (3, 5)),
  //   ((4, 0), (4, 1)), ((4, 3), (4, 4)), ((4, 4), (4, 5)),
  //   ((5, 1), (5, 2)), ((5, 3), (5, 4)), ((5, 5), (5, 6)),
  //   ((6, 0), (6, 1)), ((6, 1), (6, 2)), ((6, 2), (6, 3)), ((6, 4), (6, 5)),
  //   ((7, 1), (7, 2)), ((7, 3), (7, 4)), ((7, 5), (7, 6)),
  //   ((8, 5), (8, 6))
  // )

  //   val puzzle = new FutoshikiPuzzle(9, initialDigits, constraints)
    println("Initial Puzzle:")
    println(puzzle)




  PerformanceTest.perstart()
  ParSolver.parSolve(puzzle) match {
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