package sudoku;

import java.io.*;
import java.util.concurrent.*;

/**
 * A console interface for interaction with the Sudoku.
 */
public class Console implements Runnable {

    /**
     * @param args are ignored.
     */
    public static void main(String[] args) {
        Executor exec = Executors.newSingleThreadExecutor();
        exec.execute(new Console("./problem.txt", "./save.txt", "./solution.txt"));
    }
    private final BufferedReader input;
    private final Sudoku s;
    private final String templatePath;
    private final String savePath;
    private final String solutionPath;

    /**
     * Readies the Sudoku using data from the given path.
     *
     * @param start the path to a file with the data needed to make start the
     * Sudoku from scratch.
     * @param load the path to a file
     * @param end the path to the solution
     */
    public Console(String start, String load, String end) {
        input = new BufferedReader(new InputStreamReader(System.in));
        templatePath = start;
        savePath = load;
        solutionPath = end;

        while (true) {
            System.out.println("What would you like to do?");
            System.out.println(" - [start] over from scratch.");
            System.out.println(" - [continue] from where i left off.");
            switch (in().toLowerCase()) {
                case "start":
                    s = new Sudoku(templatePath);
                    return;
                case "continue":
                    s = new Sudoku(savePath);
                    return;
                default:
                    wrongInput();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("What would you like to do?");
            System.out.println(" - [print] the Sudoku.");
            System.out.println(" - Try to [field] solve the Sudoku.");
            System.out.println(" - Try to [char] solve the Sudoku.");
            System.out.println(" - Try to [solve] the Sudoku using all available methods.");
            System.out.println(" - [get] the possible chars for a field.");
            System.out.println(" - [fill] out a field.");
            System.out.println(" - [show] your progress.");
            System.out.println(" - [save] your progress.");
            System.out.println(" - Print the [solution].");
            System.out.println(" - [exit]");
            switch (in().toLowerCase()) {
                case "print":
                    System.out.println(s);
                    break;
                case "field":
                    System.out.println("fieldSolve() managed to fill out " + s.fieldSolve() + " fields");
                    break;
                case "char":
                    System.out.println("charSolve() managed to fill out " + s.charSolve() + " fields");
                    break;
                case "solve":
                    System.out.println("solve() managed to fill out " + s.solve() + " fields");
                    break;                                        
                case "get":
                    getChars();
                    break;
                case "fill":
                    fill();
                    break;
                case "show":
                    show();
                    break;
                case "save":
                    save();
                    break;
                case "solution":
                    solution();
                    break;
                default:
                    wrongInput();
            }
            System.out.println("==================================================");
        }
    }

    /**
     * A dialog for saving the progress.
     */
    public void save() {
        char[][] state = s.getState();

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(savePath, false);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        try {
            out.flush();
            String line = "";
            for (int row = 0; row < 9; row++) {
                line += new String(state[row]) + "\n";

            }
            out.write(line.getBytes());
            out.close();
        } catch (IOException ex) {
            System.out.println("Could not save: " + ex);
            return;
        }

        System.out.println("Save successfull.");
    }

    /**
     * A dialog for getting the possible chars for a field.
     */
    public void getChars() {
        System.out.println("What is the row of the field (1-9)?");
        int row = Integer.parseInt(in()) - 1;
        System.out.println("What is the column of the field (1-9)?");
        int col = Integer.parseInt(in()) - 1;
        System.out.println(s.getFieldChars(row, col));
    }

    /**
     * A dialogue for filling a field.
     */
    public void fill() {
        System.out.println("What is the row of the field (1-9)?");
        int row = Integer.parseInt(in()) - 1;
        System.out.println("What is the column of the field (1-9)?");
        int col = Integer.parseInt(in()) - 1;
        System.out.println("What is the value you want to fill in (1-9)?");
        char value = in().charAt(0);
        if (s.fill(value, row, col)) {
            System.out.println("Value filled.");
        } else {
            System.out.println("Could not fill in value.");
        }

    }

    /**
     * A dialog which is started when unknown input is entered.
     */
    private void wrongInput() {
        System.out.println("Please type one of the options displayed in square brackets.");
        System.out.println("You can also [exit] when you're done.\n");
    }

    /**
     * Gets input from the user.
     *
     * @return The input.
     */
    private String in() {
        System.out.print("> ");
        System.out.flush();
        try {
            String line = input.readLine();
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Bye-bye!");
                System.exit(0);
            }
            return line;
        } catch (IOException ex) {
            return ex.toString();
        }
    }

    /**
     * Loads and prints the solution.
     */
    private void solution() {
        System.out.println(new Sudoku(solutionPath));
    }

    /**
     * Prints your current progress.
     */
    private void show() {
        if (s.finished()) {
            System.out.println("Congratulations, you've completed this sudoku!");
        } else {
            System.out.printf("%d of %d fields have been filled.\n", s.progress(), s.GOAL);
        }
    }
}
