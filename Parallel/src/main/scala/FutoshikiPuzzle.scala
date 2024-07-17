package parf

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

            // Check for horizontal constraints
            if (col < size - 1) {
            if (constraints.contains(((row, col), (row, col + 1)))) sb.append(" < ")
            else if (constraints.contains(((row, col + 1), (row, col)))) sb.append(" > ")
            else sb.append(" | ")
            }
        }
        sb.append("\n")
        if (row < size - 1) {
            for (col <- 0 until size) {
            // Check for vertical constraints
            if (constraints.contains(((row, col), (row + 1, col)))) sb.append("^   ")
            else if (constraints.contains(((row + 1, col), (row, col)))) sb.append("v   ")
            else if (col < size - 1) sb.append("    ")
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

    def clonePuzzle(): FutoshikiPuzzle = {
        val newPuzzle = new FutoshikiPuzzle(size, initialDigits, constraintsInput)
        for (row <- board.indices) {
        for (col <- board(row).indices) {
            newPuzzle.board(row)(col) = this.board(row)(col)
        }
        }
        newPuzzle
    }


}


/*

{"board_size": 9, "initial_digits": {"(0, 2)": 5, "(0, 5)": 9, "(1, 3)": 5, "(2, 3)": 3, "(2, 4)": 5, "(2, 5)": 6, "(3, 4)": 8, "(6, 6)": 5, "(7, 0)": 2, "(7, 2)": 8, "(8, 2)": 6, "(8, 4)": 3, "(8, 6)": 1}, "constraints": [[[1, 2], [0, 2]], [[0, 3], [0, 4]], [[1, 8], [0, 8]], [[1, 5], [2, 5]]]}
{"board_size": 9, "initial_digits": {"(0, 2)": 5, "(0, 5)": 9, "(1, 3)": 5, "(2, 3)": 3, "(2, 4)": 5, "(2, 5)": 6, "(3, 4)": 8, "(6, 6)": 5, "(7, 0)": 2, "(7, 2)": 8, "(8, 2)": 6, "(8, 4)": 3, "(8, 6)": 1}, "constraints": [[[1, 2], [0, 2]], [[0, 3], [0, 4]], [[1, 8], [0, 8]], [[1, 5], [2, 5]], [[2, 6], [2, 5]], [[2, 7], [2, 6]], [[3, 5], [2, 5]]]}
{"board_size": 9, "initial_digits": {"(0, 2)": 5, "(0, 5)": 9, "(1, 3)": 5, "(2, 3)": 3, "(2, 4)": 5, "(2, 5)": 6, "(3, 4)": 8, "(6, 6)": 5, "(7, 0)": 2, "(7, 2)": 8, "(8, 2)": 6, "(8, 4)": 3, "(8, 6)": 1}, "constraints": [[[1, 2], [0, 2]], [[0, 3], [0, 4]], [[1, 8], [0, 8]], [[1, 5], [2, 5]], [[2, 6], [2, 5]], [[2, 7], [2, 6]], [[3, 5], [2, 5]], [[3, 2], [3, 3]], [[3, 6], [3, 5]], [[3, 0], [4, 0]]]}
{"board_size": 9, "initial_digits": {"(0, 2)": 5, "(0, 5)": 9, "(1, 3)": 5, "(2, 3)": 3, "(2, 4)": 5, "(2, 5)": 6, "(3, 4)": 8, "(6, 6)": 5, "(7, 0)": 2, "(7, 2)": 8, "(8, 2)": 6, "(8, 4)": 3, "(8, 6)": 1}, "constraints": [[[1, 2], [0, 2]], [[0, 3], [0, 4]], [[1, 8], [0, 8]], [[1, 5], [2, 5]], [[2, 6], [2, 5]], [[2, 7], [2, 6]], [[3, 5], [2, 5]], [[3, 2], [3, 3]], [[3, 6], [3, 5]], [[3, 0], [4, 0]], [[3, 1], [4, 1]], [[3, 4], [4, 4]]]}
{"board_size": 9, "initial_digits": {"(0, 2)": 5, "(0, 5)": 9, "(1, 3)": 5, "(2, 3)": 3, "(2, 4)": 5, "(2, 5)": 6, "(3, 4)": 8, "(6, 6)": 5, "(7, 0)": 2, "(7, 2)": 8, "(8, 2)": 6, "(8, 4)": 3, "(8, 6)": 1}, "constraints": [[[1, 2], [0, 2]], [[0, 3], [0, 4]], [[1, 8], [0, 8]], [[1, 5], [2, 5]], [[2, 6], [2, 5]], [[2, 7], [2, 6]], [[3, 5], [2, 5]], [[3, 2], [3, 3]], [[3, 6], [3, 5]], [[3, 0], [4, 0]], [[3, 1], [4, 1]], [[3, 4], [4, 4]], [[4, 5], [3, 5]], [[5, 6], [5, 5]], [[6, 4], [5, 4]]]}
{"board_size": 9, "initial_digits": {"(0, 2)": 5, "(0, 5)": 9, "(1, 3)": 5, "(2, 3)": 3, "(2, 4)": 5, "(2, 5)": 6, "(3, 4)": 8, "(6, 6)": 5, "(7, 0)": 2, "(7, 2)": 8, "(8, 2)": 6, "(8, 4)": 3, "(8, 6)": 1}, "constraints": [[[1, 2], [0, 2]], [[0, 3], [0, 4]], [[1, 8], [0, 8]], [[1, 5], [2, 5]], [[2, 6], [2, 5]], [[2, 7], [2, 6]], [[3, 5], [2, 5]], [[3, 2], [3, 3]], [[3, 6], [3, 5]], [[3, 0], [4, 0]], [[3, 1], [4, 1]], [[3, 4], [4, 4]], [[4, 5], [3, 5]], [[5, 6], [5, 5]], [[6, 4], [5, 4]], [[6, 2], [6, 1]], [[6, 5], [6, 4]], [[6, 7], [6, 6]], [[7, 2], [7, 1]]]}
{"board_size": 9, "initial_digits": {"(0, 2)": 5, "(0, 5)": 9, "(1, 3)": 5, "(2, 3)": 3, "(2, 4)": 5, "(2, 5)": 6, "(3, 4)": 8, "(6, 6)": 5, "(7, 0)": 2, "(7, 2)": 8, "(8, 2)": 6, "(8, 4)": 3, "(8, 6)": 1}, "constraints": [[[1, 2], [0, 2]], [[0, 3], [0, 4]], [[1, 8], [0, 8]], [[1, 5], [2, 5]], [[2, 6], [2, 5]], [[2, 7], [2, 6]], [[3, 5], [2, 5]], [[3, 2], [3, 3]], [[3, 6], [3, 5]], [[3, 0], [4, 0]], [[3, 1], [4, 1]], [[3, 4], [4, 4]], [[4, 5], [3, 5]], [[5, 6], [5, 5]], [[6, 4], [5, 4]], [[6, 2], [6, 1]], [[6, 5], [6, 4]], [[6, 7], [6, 6]], [[7, 2], [7, 1]], [[6, 8], [7, 8]], [[8, 2], [8, 1]], [[8, 7], [8, 8]]]} // use for 9x9
{"board_size": 9, "initial_digits": {"(0, 2)": 5, "(0, 5)": 9, "(1, 3)": 5, "(2, 3)": 3, "(2, 4)": 5, "(2, 5)": 6, "(3, 4)": 8, "(6, 6)": 5, "(7, 0)": 2, "(7, 2)": 8, "(8, 2)": 6, "(8, 4)": 3, "(8, 6)": 1}, "constraints": [[[1, 2], [0, 2]], [[0, 3], [0, 4]], [[1, 8], [0, 8]], [[1, 5], [2, 5]], [[2, 6], [2, 5]], [[2, 7], [2, 6]], [[3, 5], [2, 5]], [[3, 2], [3, 3]], [[3, 6], [3, 5]], [[3, 0], [4, 0]], [[3, 1], [4, 1]], [[3, 4], [4, 4]], [[4, 5], [3, 5]], [[5, 6], [5, 5]], [[6, 4], [5, 4]], [[6, 2], [6, 1]], [[6, 5], [6, 4]], [[6, 7], [6, 6]], [[7, 2], [7, 1]], [[7, 3], [7, 4]], [[7, 5], [6, 5]], [[7, 6], [6, 6]], [[6, 8], [7, 8]], [[8, 2], [8, 1]], [[8, 7], [8, 8]]]}
{"board_size": 9, "initial_digits": {"(0, 2)": 5, "(0, 5)": 9, "(1, 3)": 5, "(2, 3)": 3, "(2, 4)": 5, "(2, 5)": 6, "(3, 4)": 8, "(6, 6)": 5, "(7, 0)": 2, "(7, 2)": 8, "(8, 2)": 6, "(8, 4)": 3, "(8, 6)": 1}, "constraints": [[[1, 2], [0, 2]], [[0, 3], [0, 4]], [[1, 8], [0, 8]], [[1, 5], [2, 5]], [[2, 6], [2, 5]], [[2, 7], [2, 6]], [[3, 5], [2, 5]], [[3, 2], [3, 3]], [[3, 6], [3, 5]], [[3, 0], [4, 0]], [[3, 1], [4, 1]], [[3, 4], [4, 4]], [[4, 5], [3, 5]], [[5, 6], [5, 5]], [[6, 4], [5, 4]], [[6, 2], [6, 1]], [[6, 5], [6, 4]], [[6, 7], [6, 6]], [[7, 2], [7, 1]], [[7, 3], [7, 4]], [[7, 5], [6, 5]], [[7, 6], [6, 6]], [[6, 8], [7, 8]], [[8, 2], [8, 1]], [[8, 7], [8, 8]]]}



*/