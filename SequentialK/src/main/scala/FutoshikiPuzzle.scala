package sequentialf

class FutoshikiPuzzle(val size: Int, initialDigits: Map[(Int, Int), Int], constraintsInput: List[((Int, Int), (Int, Int))]) {
  val board: Array[Array[Int]] = Array.ofDim[Int](size, size)

  // Convert the List of constraints to a Set for efficient checking
  val constraints: Set[((Int, Int), (Int, Int))] = constraintsInput.toSet

  // Initialize board with zeros
  for (row <- board.indices; col <- board(row).indices) {
    board(row)(col) = 0
  }

  // Apply initial values
  initialDigits.foreach {
    case ((row, col), value) => board(row)(col) = value
  }

  def isValid(row: Int, col: Int, num: Int): Boolean = {
    !board(row).contains(num) && // Check row
    !board.map(_(col)).contains(num) && // Check column
    checkConstraints(row, col, num) // Check constraints
  }

  def checkConstraints(row: Int, col: Int, num: Int): Boolean = {
    constraints.forall {
      case ((r1, c1), (r2, c2)) =>
        if (row == r1 && col == c1) num < board(r2)(c2) || board(r2)(c2) == 0
        else if (row == r2 && col == c2) num > board(r1)(c1) || board(r1)(c1) == 0
        else true
    }
  }

  override def toString: String = {
    val sb = new StringBuilder
    for (row <- 0 until size) {
      for (col <- 0 until size) {
        // Append cell value or 0 if empty
        sb.append(initialDigits.getOrElse((row, col), 0))
        if (col < size - 1) {
          // Check for horizontal constraint
          val isConstraintRight = constraints.contains(((row, col), (row, col + 1)))
          if (isConstraintRight) sb.append(" < ")
          else sb.append(" | ")
        }
      }
      sb.append("\n")
      if (row < size - 1) {
        for (col <- 0 until size) {
          // Check for vertical constraint below current cell
          val isConstraintDown = constraints.contains(((row, col), (row + 1, col)))
          if (col == 0 && isConstraintDown) sb.append("    ")
          if (isConstraintDown) sb.append(" ^ ")
          else if (col < size - 1) sb.append("   ")
        }
        sb.append("\n")
      }
    }
    sb.toString
  }


  def isSolved: Boolean = {
    board.forall(row => row.forall(_ != 0)) && !isContradicted
  }

  def isContradicted: Boolean = {
    constraints.exists {
      case ((r1, c1), (r2, c2)) =>
        val val1 = board(r1)(c1)
        val val2 = board(r2)(c2)
        (val1 != 0 && val2 != 0) && (val1 >= val2)
    }
  }

  def getInitialBoard: Map[(Int, Int), Int] = initialDigits


}