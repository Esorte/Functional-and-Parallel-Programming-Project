# Functionally-and-Parallel-Programming-Project

This Project is about parallelizing the Futoshiki Solver


Our code component for both Parallel and sequential composed of 
- GUI.py: Output an GRID GUI for inputing initial number and constraints run using (sbt runp)
- Main.scala: Reading board from json file and call on Solver/ParSolver 
- FutoshikPuzzle.scala: Puzzle object that were use in the solver
- PerformanceTest.scala: returning run time after the solver had been performed.
- Solver.scala/ParSolver.scala: Solver object which use backtracking algorithm to solve Futoshiki. As for the ParSolver Also utilize backtracking however we call on future to make it run n numbers of solver concurrently. 


**Backtracking Logic:** For each cell, if it's not empty, it moves to the next cell. If it's empty, it tries each possible number, sets the number in the cell, and recursively attempts to solve the puzzle from that point. If a solution path fails, it resets the cell (backtracks) and tries the next number. If it successfully fills all cells, it returns true.


**How parrallel version work** 
- First check if the board is solved. if it is return the board
- Find the first empty cell using for loop.
- Once the first empty cell is found. Perform checker to check all the possible number that would be possible to put in the cell by checking with constraint and uniquness of the row and column
- For each of the possible number we call on Future to attemp to solve based starting with a number from possible_number
- Each future use recursive backtracking to sovle the the board.
- Once any of the future is complete the promise with solution return.


**Success Criteria** \
Achieve a significant
speedup of 5x or more in
solving Futoshiki Puzzle
using the Parallel Solver
compared to the
Sequential Solver.

**Result** \
For smaller board size the run
time of the Sequential Solver was
faster with a 4x4 board being 7x
faster than the Parallel Solver.
However, the difference in run
time become less noticeable as
the size of the board increases.
After 7x7 the Parallel Solver take
the lead and we see a speed up
of up to 7.8x for the 8x8 board.
The result can be seen more clearly in the Funpar Runtime Comparism sheet.
Our 9x9 board result is slightly
skewed as we used board with
multiple possible solution to
allow the Sequential Solver to run
faster as it was taking multiple
hours to solve the board.
The run time of the Parallel Solver
also see a massive speed up over
the Sequential Solver when there
are more constraints added.

**Conclusion** \
The Parallel Solver achieved our
success criteria and surpassed our
expectation in the speed up of the
run time. However, it seem that
with smaller workload the
Sequential Solver tends to be
faster, when more computation
power is require, the Parallel Solver
become faster which is due to the
Initialisation and Communication
overhead slow down the
Parallelisation on smaller workload.




**Note:** \
Our runtime is hughly affected by the complexness of the constraint and the grid size. \
On a 9x9 grid with less complex constrait the runtime of our sequential can be faster then the parallel version as this due to the overhead of the initalizing each future for them empty cell as mention on how the parralel version work. With the less complex constrait board there are more solution for the board, thus the sequtial was able to find a solution before the future finished or even start initalize.



