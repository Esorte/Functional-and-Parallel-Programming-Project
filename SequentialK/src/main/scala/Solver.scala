package sequentialf

object Solver {
  def solve(puzzle: FutoshikiPuzzle): Option[Array[Array[Int]]] = {
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

    if (backtrack(0, 0)) Some(puzzle.board) else None
  }
}