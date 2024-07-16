package parf

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global


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
        if (row == puzzle.size) return true // Puzzle solved
        val (nextRow, nextCol) = if (col == puzzle.size - 1) (row + 1, 0) else (row, col + 1)

        if (puzzle.board(row)(col) != 0) return backtrack(nextRow, nextCol) // Skip filled cells

        for (num <- 1 to puzzle.size) {
            if (puzzle.isValid(row, col, num)) {
            puzzle.board(row)(col) = num
            if (backtrack(nextRow, nextCol)) return true
            puzzle.board(row)(col) = 0 // Backtrack
            }
        }

        false // No solution found for this path
        }

        val future = Future {
            for (num <- possibleNumberArray) {
                val newPuzzle = puzzle.copy()
                newPuzzle.board(initial_row)(initial_col) = num
                if (backtrack(initial_row, initial_col)) return Some(newPuzzle.board)
            }
            None
        }

        Await.result(future, Duration.Inf)

    }
}