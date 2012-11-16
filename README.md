#SudokuSolver
I was bored, so I solved a Sudoku.
Then I was still bored, so I tried to solve it using Java.
So far I've implemented a method for solving by checking rows, columns and blocks for which there can only be one value.
Next up, I've implemented a method for solving by checking if a certain value can only appear on some row, column or block.
It isn't able to solve the first Sudoku by itself yet.
I've tried throwing another Sudoku at it though, and this one it can solve.
I might try out other sudokus I come across.
All tried sudokus will be divided into the folders *solved* and *not-solved*, to indicate whether the application is able to solve them or not.

Next up, I'm thinking about implementing some slightly more complex methods that use spot patterns in candidate values, in order to exclude other candidate values.
I might also have to revise the underlying data structures in order to do that.