import Futoshiki._

class FutoshikiSolver(grid: Array[Array[Option[Int]]], constraints: Seq[Constraint]) {
  val size: Int = grid.length
  val possibleValues: Array[Array[Set[Int]]] = Array.fill(size, size)(Set(1 to size: _*))

  //finding the initial number within the grid
  def initialize(): Unit = {
    for (r <- grid.indices; c <- grid.indices) {
      grid(r)(c).foreach { value =>
        setValue(Cell(r, c), value)
      }
    }
  }

  //
  def setValue(cell: Cell, value: Int): Unit = {
    grid(cell.row)(cell.col) = Some(value)
    possibleValues(cell.row)(cell.col) = Set(value)
    updateConstraints(cell, value)
  }

  def updateConstraints(cell: Cell, value: Int): Unit = {
    for (i <- grid.indices) {
      if (i != cell.col) possibleValues(cell.row)(i) -= value
      if (i != cell.row) possibleValues(i)(cell.col) -= value
    }

    constraints.foreach {
      case Constraint(c1, c2, relation) if c1 == cell =>
        relation match {
          case '<' => possibleValues(c2.row)(c2.col) = possibleValues(c2.row)(c2.col).filter(_ > value)
          case '>' => possibleValues(c2.row)(c2.col) = possibleValues(c2.row)(c2.col).filter(_ < value)
          case _ =>
        }
      case Constraint(c1, c2, relation) if c2 == cell =>
        relation match {
          case '<' => possibleValues(c1.row)(c1.col) = possibleValues(c1.row)(c1.col).filter(_ < value)
          case '>' => possibleValues(c1.row)(c1.col) = possibleValues(c1.row)(c1.col).filter(_ > value)
          case _ =>
        }
      case _ =>
    }
  }

  def solve(): Boolean = {
    val emptyCell = findEmptyCell()
    if (emptyCell.isEmpty) return true

    val cell = emptyCell.get
    for (value <- possibleValues(cell.row)(cell.col)) {
      setValue(cell, value)
      if (solve()) return true
      removeValue(cell)
    }

    false
  }

  def findEmptyCell(): Option[Cell] = {
    for (r <- grid.indices; c <- grid.indices) {
      if (grid(r)(c).isEmpty) return Some(Cell(r, c))
    }
    None
  }

  def removeValue(cell: Cell): Unit = {
    val value = grid(cell.row)(cell.col).get
    grid(cell.row)(cell.col) = None
    possibleValues(cell.row)(cell.col) = Set(1 to size: _*)

    for (i <- grid.indices) {
      if (i != cell.col && grid(cell.row)(i).isEmpty) possibleValues(cell.row)(i) += value
      if (i != cell.row && grid(i)(cell.col).isEmpty) possibleValues(i)(cell.col) += value
    }

    constraints.foreach {
      case Constraint(c1, c2, relation) if c1 == cell =>
        relation match {
          case '<' => possibleValues(c2.row)(c2.col) = possibleValues(c2.row)(c2.col).filter(_ > value)
          case '>' => possibleValues(c2.row)(c2.col) = possibleValues(c2.row)(c2.col).filter(_ < value)
          case _ =>
        }
      case Constraint(c1, c2, relation) if c2 == cell =>
        relation match {
          case '<' => possibleValues(c1.row)(c1.col) = possibleValues(c1.row)(c1.col).filter(_ < value)
          case '>' => possibleValues(c1.row)(c1.col) = possibleValues(c1.row)(c1.col).filter(_ > value)
          case _ =>
        }
      case _ =>
    }
  }


  
  def printGrid(): Unit = {
    grid.foreach { row =>
      println(row.map(_.getOrElse(".")).mkString(" "))
    }
  }
}
