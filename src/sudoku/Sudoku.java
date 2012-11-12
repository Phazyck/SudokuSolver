package sudoku;

import java.io.*;

public class Sudoku {

    private final char[][] state = new char[9][9];
    private final char empty = '.';
    private final char[] values = "123456789".toCharArray();
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

    @Override
    public String toString() {
        String string = "";
        for (int segX = 0; segX < 3; segX++) {
            for (int subSegX = 0; subSegX < 3; subSegX++) {
                for (int segY = 0; segY < 3; segY++) {
                    for (int subSegY = 0; subSegY < 3; subSegY++) {
                        char x = state[segX * 3 + subSegX][segY * 3 + subSegY];
                        String s = x == 0 ? " " : "" + x;
                        string += s + " ";
                    }

                    if (segY < 2) {
                        string += "|";
                    }

                }
                string += "\n";
            }
            if (segX < 2) {
                string += "------+------+------\n";
            }
        }

        return string;
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
        for (char[] arr : state) {
            CharSet line = new CharSet(arr);
            line.remove(empty);
            progress += line.size();
        }
        return progress;
    }

    public boolean finished() {
        return progress() == GOAL;
    }

    public int solve() {
        int found = 0;
        int last;
        do {
            last = found;

            found += fieldSolve() + charSolve();            
        } while (found != last);

        return found;

    }

    /**
     * Attempt to solve the Sudoku by filling fields where, at first glance,
     * there is only one possible character to insert.
     *
     * @return the number of new entries.
     */
    public int fieldSolve() {
        int found = 0;
        int last;
        do {
            last = found;

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    CharSet chars = getFieldChars(i, j);
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
     * Attempt to solve the Sudoku by filling a character into the only field
     * inside a row, column or block, where it can be.
     *
     * @return the number of new entries.
     */
    public int charSolve() {
        int found = 0;
        int last;
        do {
            last = found;

            for (char c : values) {
                found += fillChar(c);
            }
        } while (found != last);

        return found;
    }

    /**
     * Get a set of the chars that can be placed in the given field.
     *
     * @param row The row of the field.
     * @param col The column of the field.
     * @return A CharSet containing the available chars.
     */
    public CharSet getFieldChars(int row, int col) {
        if (state[row][col] == empty) {
            return CharSet.intersection(CharSet.intersection(getRowChars(row), getColChars(col)), getBlockChars(row, col));
        } else {
            return new CharSet();
        }
    }

    /**
     * Get a set of all chars that have already been entered in this row.
     *
     * @param row The row.
     * @return A CharSet containing the used chars.
     */
    private CharSet getRowChars(int row) {
        CharSet chars = new CharSet(values);
        for (int col = 0; col < 9; col++) {
            chars.remove(state[row][col]);
        }
        //System.out.println("[" + row + "] : " + chars);
        return chars;
    }

    /**
     * Get a set of all chars that have already been entered in this column.
     *
     * @param col The column.
     * @return A CharSet containing the used chars.
     */
    private CharSet getColChars(int col) {
        CharSet chars = new CharSet(values);
        for (int row = 0; row < 9; row++) {
            chars.remove(state[row][col]);
        }
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
        CharSet chars = new CharSet(values);
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                chars.remove(state[(row / 3) * 3 + a][(col / 3) * 3 + b]);
            }
        }
        //System.out.println("[" + row + "," + col + "] : " + chars);
        return chars;
    }

    private int fillChar(char c) {
        int fills = 0;

        for (int i = 0; i < 9; i++) {
            if (fillCol(i, c)) {
                fills++;
            }
            if (fillRow(i, c)) {
                fills++;
            }
            if (i % 3 == 0) {
                if (fillBlock(i, i, c)) {
                    fills++;
                }

            }
        }

        return fills;
    }

    private boolean fillCol(int col, char c) {
        int hitRow = -1;
        for (int row = 0; row < 9; row++) {
            if (getFieldChars(row, col).contains(c)) {
                if (hitRow == -1) {
                    hitRow = row;
                } else {
                    return false;
                }
            }
        }

        if (hitRow == -1) {
            return false;
        }

        fill(c, hitRow, col);
        return true;
    }

    private boolean fillRow(int row, char c) {
        int hitCol = -1;
        for (int col = 0; col < 9; col++) {
            if (getFieldChars(row, col).contains(c)) {
                if (hitCol == -1) {
                    hitCol = col;
                } else {
                    return false;
                }
            }
        }

        if (hitCol == -1) {
            return false;
        }

        fill(c, row, hitCol);
        return true;
    }

    private boolean fillBlock(int row, int col, char c) {
        int blockRow = (row / 3) * 3;
        int blockCol = (col / 3) * 3;

        int hitRow = -1;
        int hitCol = -1;

        for (int dRow = 0; dRow < 3; dRow++) {
            for (int dCol = 0; dCol < 3; dCol++) {
                if (getFieldChars(blockRow + dRow, blockCol + dCol).contains(c)) {
                    if (hitRow == -1 && hitCol == -1) {
                        hitRow = blockRow + dRow;
                        hitCol = blockCol + dCol;
                    } else {
                        return false;
                    }
                }
            }
        }

        if (hitRow == -1 && hitCol == -1) {
            return false;
        }

        fill(c, hitRow, hitCol);
        return true;
    }
}
