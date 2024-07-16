package parf

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import scala.util.Try


object ParSolver {
    def parSolve(puzzle: FutoshikiPuzzle): Option[Array[Array[Int]]] = {

        if (puzzle.board(0)(0) != 0) return Some(puzzle.board) // Puzzle already solved

        var possibleNumber: ArrayBuffer[Int] = ArrayBuffer[Int]()

        var initial_row = 0
        var initial_col = 0

        while (puzzle.board(initial_row)(initial_col) != 0) {
            if (initial_col == puzzle.size - 1) {
                initial_row += 1
                initial_col = 0
            } else {
                initial_col += 1
            }
        };

        for (num <- 1 to puzzle.size) {
            if (puzzle.isValid(initial_row, initial_col, num)) {
                possibleNumber += num
            }
        };

        val possibleNumberArray = possibleNumber.toArray

        def backtrack(row: Int, col: Int): Boolean = {
            if (row == puzzle.size) true // Puzzle solved
            else {
                val (nextRow, nextCol) = if (col == puzzle.size - 1) (row + 1, 0) else (row, col + 1)

                if (puzzle.board(row)(col) != 0) backtrack(nextRow, nextCol) // Skip filled cells
                else {
                var found = false
                for (num <- 1 to puzzle.size if !found) {
                    if (puzzle.isValid(row, col, num)) {
                    puzzle.board(row)(col) = num
                    if (backtrack(nextRow, nextCol)) found = true
                    else puzzle.board(row)(col) = 0 // Backtrack
                    }
                }
                found
                }
            }
        }


        val futures = possibleNumber.map { num =>
            Future {
                puzzle.board(initial_row)(initial_col) = num
                if (backtrack(initial_row, initial_col)) puzzle else None
                // println(s"Board with num $num at position ($initial_row, $initial_col):")
                // puzzle.board.foreach(row => println(row.mkString(" ")))

            }
        }

        // Await all futures and return the first successful result
        Try(Await.result(Future.firstCompletedOf(futures), Duration.Inf)) match {
            case Success(_) => Some(puzzle.board)
            case _ => None // No solution found
        }

    }
}