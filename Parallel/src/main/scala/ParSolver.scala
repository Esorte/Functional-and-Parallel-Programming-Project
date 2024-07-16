package sequentialpar



object ParSolver {
    def parSolve(puzzle: FutoshikiPuzzle): Option[Array[Array[Int]]] = {

        if (puzzle.board(0)(0) != 0) return Some(puzzle.board) // Puzzle already solved

        val possibleNumber = Array.fill(puzzle.size, puzzle.size)(Set.empty[Int])

        for (row <- puzzle.board.indices; col <- puzzle.board(row).indices) {
            if (puzzle.board(row)(col) == 0) {
                possibleNumber(row)(col) = (1 to puzzle.size).toSet
            }
        }


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

        if (backtrack(0, 0)) Some(puzzle.board) else None
    }
}