# Functionally-and-Parallel-Programming-Project

This Project is about parallelizing the Futoshiki Solver


Our code component for both Parallel and sequential composed of 
- GUI.py: Output an GRID GUI for inputing initial number and constraints run using (sbt runp)
- Main.scala: Reading board from json file and call on Solver/ParSolver 
- FutoshikPuzzle.scala: Puzzle object that were use in the solver
- PerformanceTest.scala: returning run time after the solver had been performed.
- Solver.scala/ParSolver.scala: Solver object which use backtracking algorithm to solve Futoshiki. As for the ParSolver Also utilize backtracking however we call on future to make it run n numbers of solver concurrently. 
