# Functionally-and-Parallel-Programming-Project

This Project is about parallelizing the Futoshiki Solver


Our code component for both Parallel and sequential composed of 
- GUI.py: Output an GRID GUI for inputing initial number and constraints run using (sbt runp)
- Main.scala: Reading board from json file and call on Solver/ParSolver 
- FutoshikPuzzle.scala: Puzzle object that were use in the solver
- PerformanceTest.scala: returning run time after the solver had been performed.
- Solver.scala/ParSolver.scala: Solver object which use backtracking algorithm to solve Futoshiki. As for the ParSolver Also utilize backtracking however we call on future to make it run n numbers of solver concurrently. 


**Backtracking Logic:** For each cell, if it's not empty, it moves to the next cell. If it's empty, it tries each possible number, sets the number in the cell, and recursively attempts to solve the puzzle from that point. If a solution path fails, it resets the cell (backtracks) and tries the next number. If it successfully fills all cells, it returns true.


**How parrallel version work** \
- First check if the board is solved. if it is return the board
- Find the first empty cell using for loop.
- Once the first empty cell is found. Perform checker to check all the possible number that would be possible to put in the cell by checking with constraint and uniquness of the row and column
- For each of the possible number we call on Future to attemp to solve based starting with a number from possible_number
- Each future use recursive backtracking to sovle the the board.
- Once any of the future is complete the promise with solution return.
