object Futoshiki {
    case class Cell(row: Int, col: Int)
    case class Constraint(cell1: Cell, cell2: Cell, relation: Char)
}