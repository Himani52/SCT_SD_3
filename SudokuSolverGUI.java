import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuSolverGUI extends JFrame {
    private static final int SIZE = 9;
    private JTextField[][] cells = new JTextField[SIZE][SIZE];
    private JButton solveButton, clearButton;

    public SudokuSolverGUI() {
        setTitle("Sudoku Solver");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(SIZE, SIZE));

        // Create grid cells with alternating colors for 3x3 boxes
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 24));

                // Alternate background color for 3x3 boxes
                if ((row / 3 + col / 3) % 2 == 0) {
                    cells[row][col].setBackground(new Color(220, 220, 220));
                } else {
                    cells[row][col].setBackground(Color.WHITE);
                }

                gridPanel.add(cells[row][col]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        solveButton = new JButton("Solve Sudoku");
        clearButton = new JButton("Clear Grid");

        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Solve button action
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] grid = new int[SIZE][SIZE];
                boolean[][] userInput = new boolean[SIZE][SIZE];

                // Read input from text fields
                for (int row = 0; row < SIZE; row++) {
                    for (int col = 0; col < SIZE; col++) {
                        String text = cells[row][col].getText();
                        if (text.isEmpty()) {
                            grid[row][col] = 0;
                        } else {
                            try {
                                int num = Integer.parseInt(text);
                                if (num < 1 || num > 9) throw new NumberFormatException();
                                grid[row][col] = num;
                                userInput[row][col] = true; // mark as user input
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null,
                                        "Invalid input at row " + (row + 1) + " column " + (col + 1));
                                return;
                            }
                        }
                    }
                }

                // Solve the puzzle
                if (solveSudoku(grid)) {
                    // Display solved puzzle
                    for (int row = 0; row < SIZE; row++) {
                        for (int col = 0; col < SIZE; col++) {
                            cells[row][col].setText(String.valueOf(grid[row][col]));
                            if (!userInput[row][col]) {
                                cells[row][col].setForeground(Color.BLUE); // solved numbers in blue
                            } else {
                                cells[row][col].setForeground(Color.BLACK); // original input in black
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No solution exists for this puzzle.");
                }
            }
        });

        // Clear button action
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int row = 0; row < SIZE; row++) {
                    for (int col = 0; col < SIZE; col++) {
                        cells[row][col].setText("");
                        cells[row][col].setForeground(Color.BLACK);
                    }
                }
            }
        });
    }

    // Sudoku solver using backtracking
    private boolean solveSudoku(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (solveSudoku(grid)) return true;
                            grid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSafe(int[][] grid, int row, int col, int num) {
        // Check row
        for (int x = 0; x < SIZE; x++)
            if (grid[row][x] == num) return false;

        // Check column
        for (int x = 0; x < SIZE; x++)
            if (grid[x][col] == num) return false;

        // Check 3x3 box
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (grid[startRow + i][startCol + j] == num) return false;

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuSolverGUI frame = new SudokuSolverGUI();
            frame.setVisible(true);
        });
    }
}
