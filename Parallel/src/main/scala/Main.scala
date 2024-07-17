package parf
import scala.io.Source
import io.circe._, io.circe.parser._, io.circe.generic.auto._

object Main extends App {
  val f = "puzzle_data"; // default puzzle_data

  //Custom KeyDecoder for (Int, Int) tuples
  implicit val decodeMapKey: KeyDecoder[(Int, Int)] = new KeyDecoder[(Int, Int)] {
    override def apply(key: String): Option[(Int, Int)] = {
      key.trim.drop(1).dropRight(1).split(",").map(_.trim.toIntOption) match {
        case Array(Some(x), Some(y)) => Some((x, y))
        case _ => None
      }
    }
  }

  // Read the JSON file
  val source = Source.fromFile(s"${f}.json")
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


//* 4x4 WORK
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

//* 5x5 WORK
// val initialDigits = Map(
//   (4, 0) -> 1, (4, 4) -> 4,
// )

// val constraints = List(
//   ((0, 1), (1, 1)),
//   ((1, 1), (2, 1)),
//   ((2, 1), (2, 0)), 
//   ((2, 0), (3, 0)),
//   ((3, 2), (2, 2)),
//   ((1, 3), (1, 2)), 
//   ((2, 4), (3, 4))
// )

//   val puzzle = new FutoshikiPuzzle(5, initialDigits, constraints)
//   println("Initial Puzzle:")
//   println(puzzle)

  //*****************************************\\
//* 6x6 WORK
// val initialDigits = Map(
//   (4, 0) -> 4, (5, 2) -> 5,
// )

// val constraints = List(
//   ((0, 0), (0, 1)), ((0, 4), (0, 3)), ((0, 5), (0, 4)), 
//   ((1, 5), (0, 5)), ((1, 2), (1, 3)), ((1, 3), (1, 4)), 
//   ((2, 1), (1, 1)), ((2, 3), (2, 2)), ((2, 4), (2, 3)), 
//   ((3, 1), (2, 1)), ((3, 3), (3, 4)), ((3, 4), (2, 4)), 
//   ((4, 5), (3, 5)), 
//   ((5, 3), (4, 3)), 
//   ((5, 5), (5, 4))
// )

//   val puzzle = new FutoshikiPuzzle(6, initialDigits, constraints)
//   println("Initial Puzzle:")
//   println(puzzle)

  //*****************************************\\

  //* 7x7 WORK
// val initialDigits = Map(
//   (1, 1) -> 1, (1, 2) -> 6,
//   (2, 2) -> 2,
//   (3, 6) -> 6,
//   (4, 6) -> 4,
//   (5, 2) -> 3, (5, 5) -> 4,
//   (6, 0) -> 3, (6, 1) -> 5
// )

// val constraints = List( 
//   ((1, 0), (0, 0)), ((1, 3), (0, 3)), ((1, 4), (0, 3)),   
//   ((2, 3), (1, 3)), 
//   ((3, 4), (2, 4)), ((3, 5), (3, 4)),  
//   ((3, 1), (4, 1)), 
//   ((5, 1), (5, 0)), ((4, 1), (5, 1)), ((4, 2), (5, 2)), ((5, 3), (4, 3)), ((5, 4), (4, 4)), 
//   ((6, 1), (6, 2)), ((5, 6), (6, 6))
// )

//   val puzzle = new FutoshikiPuzzle(7, initialDigits, constraints)
//   println("Initial Puzzle:")
//   println(puzzle)

//*****************************************\\

// val initialDigits = Map(
//   (0, 0) -> 8, 
//   (1, 2) -> 5,
//   (3, 1) -> 3, (3, 6) -> 5,
//   (5, 1) -> 2, (5, 6) -> 4, (5, 7) -> 8,
//   (7, 4) -> 6, (7, 6) -> 2
// )

// val constraints = List( 
//   ((1, 0), (0, 0)) 
// )

//   val puzzle = new FutoshikiPuzzle(8, initialDigits, constraints)
  println("Initial Puzzle:")
  println(puzzle)



  // (1 to 10).foreach { _ =>
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
  // }
}