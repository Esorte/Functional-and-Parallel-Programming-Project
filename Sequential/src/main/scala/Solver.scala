package sequentialf

object Solver {

  def solve(puzzle: FutoshikiPuzzle): Option[Array[Array[Int]]] = {
    def findEmptyCell(): Option[(Int, Int)] = {
      for (row <- 0 until puzzle.size; col <- 0 until puzzle.size if puzzle.board(row)(col) == 0) {
        return Some((row, col))
      }
      None
    }

    def isValid(num: Int, row: Int, col: Int): Boolean = {
      // Check row uniqueness
      for (c <- 0 until puzzle.size) {
        if (puzzle.board(row)(c) == num) return false
      }
      // Check column uniqueness
      for (r <- 0 until puzzle.size) {
        if (puzzle.board(r)(col) == num) return false
      }
      // Set number to check constraints
      puzzle.board(row)(col) = num
      val isValidConstraint = !puzzle.isContradicted
      puzzle.board(row)(col) = 0 // Reset after check
      isValidConstraint
    }

    def setValue(num: Int, row: Int, col: Int): Unit = {
      puzzle.board(row)(col) = num
    }

    def resetValue(row: Int, col: Int): Unit = {
      puzzle.board(row)(col) = 0
    }

    def solveRec(): Boolean = {
      val emptyCell = findEmptyCell()
      emptyCell match {
        case Some((row, col)) =>
          (1 to puzzle.size).foreach { num =>
            if (isValid(num, row, col)) {
              setValue(num, row, col)
              if (solveRec()) return true
              resetValue(row, col)
            }
          }
          false
        case None => true
      }
    }

    if (solveRec()) Some(puzzle.board) else None
  }
}