package parf

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Future, Promise, Await}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}
import scala.util.control.Breaks._

object ParSolver {
  def parSolve(puzzle: FutoshikiPuzzle): Option[Array[Array[Int]]] = {
    if (puzzle.board(0)(0) != 0) return Some(puzzle.board) // Puzzle already solved

    var possibleNumber: ArrayBuffer[Int] = ArrayBuffer[Int]()
    var initial_row = 0
    var initial_col = 0

    // Find the first empty cell
    while (puzzle.board(initial_row)(initial_col) != 0) {
      if (initial_col == puzzle.size - 1) {
        initial_row += 1
        initial_col = 0
      } else {
        initial_col += 1
      }
    }

    // Collect all valid numbers for the first empty cell
    for (num <- 1 to puzzle.size) {
      if (puzzle.isValid(initial_row, initial_col, num)) {
        possibleNumber += num
      }
    }

    val solutionPromise = Promise[Option[Array[Array[Int]]]]()

    possibleNumber.foreach { num =>
      Future {
        if (!solutionPromise.isCompleted) {
          val newPuzzle = puzzle.clonePuzzle() // Clone the puzzle to avoid shared state issues
          newPuzzle.board(initial_row)(initial_col) = num
          // println(s"Board with num $num at position ($initial_row, $initial_col):")
          // puzzle.board.foreach(row => println(row.mkString(" ")))
          if (backtrack(newPuzzle, initial_row, initial_col)) {
            solutionPromise.trySuccess(Some(newPuzzle.board))
          }
        }
      }
    }

    Try(Await.result(solutionPromise.future, Duration.Inf)) match {
      case Success(result) => result
      case Failure(_) => None // No solution found or error occurred
    }
  }

  def backtrack(puzzle: FutoshikiPuzzle, row: Int, col: Int): Boolean = {
    if (row == puzzle.size) true // Puzzle solved
    else {
      val (nextRow, nextCol) = if (col == puzzle.size - 1) (row + 1, 0) else (row, col + 1)

      if (puzzle.board(row)(col) != 0) backtrack(puzzle, nextRow, nextCol) // Skip filled cells
      else {
        var result = false
        breakable {
          for (num <- 1 to puzzle.size) {
            if (puzzle.isValid(row, col, num)) {
              puzzle.board(row)(col) = num
              if (backtrack(puzzle, nextRow, nextCol)) {
                result = true
                break
              }
              puzzle.board(row)(col) = 0 // Backtrack
            }
          }
        }
        result
      }
    }
  }
}