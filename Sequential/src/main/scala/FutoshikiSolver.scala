import Futoshiki._
import scala.collection.mutable


case class Cell(row: Int, col: Int)
case class Constraint(c1: Cell, c2: Cell, relation: Char)

class FutoshikiSolver(val grid: Array[Array[Option[Int]]], val constraints: Seq[Constraint]) {
  val size: Int = grid.length
  val possibleValues: Array[Array[Set[Int]]] = Array.fill(size, size)(Set(1 to size: _*))

  /*
*  possibleValues =
*  Array(
*     Array(Set(1, 2, 3, 4), Set(1, 2, 3, 4), Set(1, 2, 3, 4), Set(1, 2, 3, 4)),
*     Array(Set(1, 2, 3, 4), Set(1, 2, 3, 4), Set(1, 2, 3, 4), Set(1, 2, 3, 4)),
*     Array(Set(1, 2, 3, 4), Set(1, 2, 3, 4), Set(1, 2, 3, 4), Set(1, 2, 3, 4)),
*     Array(Set(1, 2, 3, 4), Set(1, 2, 3, 4), Set(1, 2, 3, 4), Set(1, 2, 3, 4))
*   )
*/

  def initialize(): Unit = {
  // Initialize the grid with the starting values and apply constraints
  for (r <- grid.indices; c <- grid.indices) {
    grid(r)(c).foreach { value =>
      setValue(Cell(r, c), value)
      }
    }
    updateConstraints()
  }

  //setting value in each index of the grid and update values in possibleValues
  def setValue(cell: Cell, value: Int): Unit = {
    grid(cell.row)(cell.col) = Some(value)
    updateConstraints()
  }

  //
  def updateConstraints(): Unit = {
    var updated = true
    while (updated) {
      updated = false
      for (constraint <- constraints) {
        val Constraint(c1, c2, relation) = constraint
        val beforeUpdate1 = possibleValues(c1.row)(c1.col)
        val beforeUpdate2 = possibleValues(c2.row)(c2.col)
        relation match {
          case '<' =>
            possibleValues(c1.row)(c1.col) = beforeUpdate1.filter(v1 => beforeUpdate2.exists(v2 => v1 < v2))
            possibleValues(c2.row)(c2.col) = beforeUpdate2.filter(v2 => beforeUpdate1.exists(v1 => v1 < v2))
          case '>' =>
            possibleValues(c1.row)(c1.col) = beforeUpdate1.filter(v1 => beforeUpdate2.exists(v2 => v1 > v2))
            possibleValues(c2.row)(c2.col) = beforeUpdate2.filter(v2 => beforeUpdate1.exists(v1 => v1 > v2))
        }
        if (beforeUpdate1 != possibleValues(c1.row)(c1.col) || beforeUpdate2 != possibleValues(c2.row)(c2.col)) {
          updated = true
        }
      }
    }
  }

  def solve(): Boolean = {
    if (isSolved) return true
    val cell = findEmptyCell().getOrElse(return false)
    for (value <- possibleValues(cell.row)(cell.col)) {
      if (isValid(cell, value)) {
        setValue(cell, value)
        if (solve()) return true
        removeValue(cell)
      }
    }
    false
  }

  def isSolved: Boolean = grid.forall(_.forall(_.isDefined))

  def findEmptyCell(): Option[Cell] = {
    grid.indices.flatMap { r =>
      grid.indices.collectFirst {
        case c if grid(r)(c).isEmpty && possibleValues(r)(c).nonEmpty => Cell(r, c)
      }
    }.reduceOption((cell1, cell2) => if (possibleValues(cell1.row)(cell1.col).size < possibleValues(cell2.row)(cell2.col).size) cell1 else cell2)
  }

  def removeValue(cell: Cell): Unit = {
    val value = grid(cell.row)(cell.col).get
    grid(cell.row)(cell.col) = None
    possibleValues(cell.row)(cell.col) = Set(1 to size: _*) // Reset to all possible values

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
    updateConstraints()
  }


  def isValid(cell: Cell, value: Int): Boolean = {
    // Check row and column
    for (i <- 0 until size) {
      if (grid(cell.row)(i).contains(value) || grid(i)(cell.col).contains(value)) return false
    }
    // Check constraints
    constraints.foreach {
      case Constraint(c1, c2, relation) if c1 == cell || c2 == cell =>
        val otherCell = if (c1 == cell) c2 else c1
        grid(otherCell.row)(otherCell.col) match {
          case Some(otherValue) =>
            relation match {
              case '<' if c1 == cell && value >= otherValue => return false
              case '>' if c1 == cell && value <= otherValue => return false
              case '<' if c2 == cell && otherValue >= value => return false
              case '>' if c2 == cell && otherValue <= value => return false
              case _ =>
            }
          case None =>
        }
      case _ =>
    }
    true
  }

  def printGrid(): Unit = {
    grid.foreach { row =>
      println(row.map(_.getOrElse(".")).mkString(" "))
    }
  }

}
