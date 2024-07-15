package sequentialpar

import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._



object ParSolver {
    def parSolve(puzzle: FutoshikiPuzzle): Option[Array[Array[Int]]] = {
        def backtrack(row: Int, col: Int): Boolean = {
            if (row == puzzle.size) return true // Puzzle solved
            val (nextRow, nextCol) = if (col == puzzle.size - 1) (row + 1, 0) else (row, col + 1)

            if (puzzle.board(row)(col) != 0) return backtrack(nextRow, nextCol) // Skip filled cells

            val futures = for (num <- 1 to puzzle.size) yield Future {
                if (puzzle.isValid(row, col, num)) {
                    puzzle.board(row)(col) = num
                    if (backtrack(nextRow, nextCol)) return true
                    puzzle.board(row)(col) = 0 // Backtrack
                }
                false // No solution found for this path
            }

            val futureResult = Future.firstCompletedOf(futures)
            val result = Await.result(futureResult, 1.second)

            result
        }

        if (backtrack(0, 0)) Some(puzzle.board) else None
    }
}