# Functionally-and-Parallel-Programming-Project

This Project is about parallelizing the Futoshiki Solver


Our code component for both Parallel and sequential composed of 
- GUI.py: Output an GRID GUI for inputing initial number and constraints run using (sbt runp)
- Main.scala: Reading board from json file and call on Solver/ParSolver 
- FutoshikPuzzle.scala: Puzzle object that were use in the solver
- PerformanceTest.scala: returning run time after the solver had been performed.
- Solver.scala/ParSolver.scala: Solver object which use backtracking algorithm to solve Futoshiki. As for the ParSolver Also utilize backtracking however we call on future to make it run n numbers of solver concurrently. 


Backtracking Logic: For each cell, if
it's not empty, it moves to the next
cell. If it's empty, it tries each possible
number, sets the number in the cell,
and recursively attempts to solve the
puzzle from that point. If a solution
path fails, it resets the cell
(backtracks) and tries the next
number. If it successfully fills all cells,
it returns true
