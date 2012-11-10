package sudoku;

import java.io.*;

public class Sudoku {

    private final char[][] state = new char[9][9];
    private final char empty = '0';
    public final int GOAL = 81;

    /**
     * Construct a Sudoku from a two-dimensional array of filled fields.
     *
     * @param filled the filled array.
     * @param empty the char which indicates that a field is empty.
     */
    public Sudoku(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            for (int row = 0; row < 9; row++) {
                System.arraycopy(reader.readLine().toCharArray(), 0, state[row], 0, 9);
            }
        } catch (IOException ex) {
            System.out.println("An error has occured: " + ex);
        }
    }

    /**
     * Print the current layout of the Sudoku.
     */
    public void print() {
        System.out.println("+-------+-------+-------+");
        for (int segX = 0; segX < 3; segX++) {
            for (int subSegX = 0; subSegX < 3; subSegX++) {
                System.out.print("|");
                for (int segY = 0; segY < 3; segY++) {
                    for (int subSegY = 0; subSegY < 3; subSegY++) {
                        char x = state[segX * 3 + subSegX][segY * 3 + subSegY];
                        String s = x == 0 ? " " : "" + x;
                        System.out.print(s);
                        if (subSegY < 2) {
                            System.out.print("  ");
                        }
                    }

                    System.out.print("|");

                }
                System.out.println();
            }
            System.out.println("+-------+-------+-------+");
        }
    }

    /**
     * Fills a given field with the given value.
     *
     * @param value the value.
     * @param row the row of the field.
     * @param col the column of the field.
     */
    public boolean fill(char value, int row, int col) {
        if (state[row][col] == empty) {
            state[row][col] = value;
            return true;
        } else {
            return false;
        }
    }

    public char[][] getState() {
        return state;
    }
    
    public int progress() {
        int progress = 0;
        for(char[] arr : state) {
            CharSet line = new CharSet(arr);
            line.remove(empty);            
            progress += line.size();
        }
        return progress;
    }
    
    public boolean finished() {
        return progress() == GOAL;
    }

    /**
     * Attempt to solve the Sudoku in a simple way.
     *
     * @return the number of new entries.
     */
    public int simpleSolve() {
        int found = 0;
        int last;
        do {
            last = found;

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    CharSet chars = getFreeChars(i, j);
                    if (chars.size() == 1) {
                        found++;
                        fill(chars.peek(), i, j);
                        //System.out.println(chars + " -> [" + i + "," + j + "]");
                    }
                }
            }
        } while (found != last);
        return found;
    }

    /**
     * Attempt to solve the Sudoku in a more sophisticated way.
     *
     * @return the number of new entries.
     */
    public int complexSolve() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Get a set of the chars that can be placed in the given field.
     *
     * @param row The row of the field.
     * @param col The column of the field.
     * @return A CharSet containing the available chars.
     */
    public CharSet getFreeChars(int row, int col) {
        if (state[row][col] != empty) {
            return new CharSet();
        }

        return CharSet.complement(new CharSet("123456789"), getUsedChars(row, col));
    }

    /**
     * Get a set of the chars that can't be placed in the given field.
     *
     * @param row The row of the field.
     * @param col The column of the field.
     * @return A CharSet containing the used chars.
     */
    private CharSet getUsedChars(int row, int col) {
        return CharSet.union(CharSet.union(getHorizontalChars(row), getVerticalChars(col)), getBlockChars(row, col));
    }

    /**
     * Get a set of all chars that have already been entered in this row.
     *
     * @param row The row.
     * @return A CharSet containing the used chars.
     */
    private CharSet getHorizontalChars(int row) {
        CharSet chars = new CharSet();
        for (int col = 0; col < 9; col++) {
            chars.add(state[row][col]);
        }
        chars.remove(empty);
        //System.out.println("[" + row + "] : " + chars);
        return chars;
    }

    /**
     * Get a set of all chars that have already been entered in this column.
     *
     * @param col The column.
     * @return A CharSet containing the used chars.
     */
    private CharSet getVerticalChars(int col) {
        CharSet chars = new CharSet();
        for (int row = 0; row < 9; row++) {
            chars.add(state[row][col]);
        }
        chars.remove(empty);
        //System.out.println("[" + col + "] : " + chars);
        return chars;
    }

    /**
     * Get a set of all chars that have already been entered in the 3x3 block
     * containing the given field.
     *
     * @param row The row of the field.
     * @param col The column of the field.
     * @return A CharSet containing the used chars.
     */
    private CharSet getBlockChars(int row, int col) {
        CharSet chars = new CharSet();
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                chars.add(state[(row / 3) * 3 + a][(col / 3) * 3 + b]);
            }
        }
        chars.remove(empty);
        //System.out.println("[" + row + "," + col + "] : " + chars);
        return chars;
    }
}
