package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

class Maze {
    private final List<List<Integer>> grid;

    public Maze() {
        this.grid = new ArrayList<>();
    }

    public void addRow(String row) {
        List<Integer> rowData = new ArrayList<>();
        for (int idx = 0; idx < row.length(); idx++) {
            char cell = row.charAt(idx);
            if (cell == '#') {
                rowData.add(1); // Wall
            } else if (cell == ' ') {
                rowData.add(0); // Path tile
            } else {
                throw new IllegalArgumentException("Invalid character in maze: " + cell);
            }
        }
        grid.add(rowData);
    }

    public List<List<Integer>> getGrid() {
        return grid;
    }

    public void display() {
        for (List<Integer> row : grid) {
            for (int cell : row) {
                if (cell == 1) {
                    System.out.print("WALL ");
                } else if (cell == 0) {
                    System.out.print("PASS ");
                }
            }
            System.out.println();
        }
    }
}

class MazeReader {
    public static Maze readFromFile(String filePath) throws IOException {
        Maze maze = new Maze();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                maze.addRow(line);
            }
        }
        return maze;
    }
}

class Explorer {
    protected Maze maze;
    protected int startRow;
    protected String canonicalPath;
    protected String factorizedPath;

    public Explorer(Maze maze) {
        this.maze = maze;
        var grid = maze.getGrid();

        // Find the starting position
        startRow = findStartRow(grid);

        // Generate the path
        canonicalPath = generatePath(grid, startRow);

        // Convert to factorized path
        factorizedPath = convertPath();
    }

    protected int findStartRow(List<List<Integer>> grid) {
        for (int row = 0; row < grid.size(); row++) {
            if (grid.get(row).get(0) == 0) {
                return row;
            }
        }
        throw new IllegalArgumentException("No valid start found in the maze!");
    }

    // initial path generation for mvp
    // assumes the maze is a direct path, (no dead ends)
    protected String generatePath(java.util.List<java.util.List<Integer>> grid, int startRow) {
        StringBuffer path = new StringBuffer();
        int[] currentDir = {1, 0}; // initially facing right
        int currentRow = startRow;
        int currentCol = 0;

        while (currentCol < grid.get(0).size() - 1) {
            
            if (grid.get(currentRow-currentDir[1]).get(currentCol+currentDir[0]) == 0) {
                path.append('F');
                currentRow -= currentDir[1];
                currentCol += currentDir[0];
            } else {
                if (currentDir[0] == 1) {
                    if (grid.get(currentRow+1).get(currentCol) == 0) {
                        currentDir[0] = 0;
                        currentDir[1] = -1;
                        path.append(" R ");
                    } else {
                        currentDir[0] = 0;
                        currentDir[1] = 1;
                        path.append(" L ");
                    }
                } else if (currentDir[1] == -1) {
                    if (grid.get(currentRow).get(currentCol+1) == 0) {
                        currentDir[0] = 1;
                        currentDir[1] = 0;
                        path.append(" L ");
                    } else {
                        currentDir[0] = -1;
                        currentDir[1] = 0;
                        path.append(" R ");
                    }
                } else if (currentDir[0] == -1) {
                    if (grid.get(currentRow+1).get(currentCol) == 0) {
                        currentDir[0] = 0;
                        currentDir[1] = -1;
                        path.append(" L ");
                    } else {
                        currentDir[0] = 0;
                        currentDir[1] = 1;
                        path.append(" R ");
                    }
                } else if (currentDir[1] == 1) {
                    if (grid.get(currentRow).get(currentCol+1) == 0) {
                        currentDir[0] = 1;
                        currentDir[1] = 0;
                        path.append(" R ");
                    } else {
                        currentDir[0] = -1;
                        currentDir[1] = 0;
                        path.append(" L ");
                    }
                }
            }
        }

        return path.toString();
    }

    protected String convertPath() {
        StringBuffer path = new StringBuffer();
        int countF = 0;
        int countR = 0;
        int countL = 0;
        char previousChar = '\0';

        for (int i = 0; i < canonicalPath.length(); i++) {
            char currentChar = canonicalPath.charAt(i);

            if (currentChar == ' ') {
                continue;
            }

            if (currentChar == 'F') {
                countF++;
            } else if (previousChar == 'F') {
                if (countF > 0) {
                    if (countF > 1) {
                        path.append(countF).append("F ");
                    } else {
                        path.append("F ");
                    }
                    countF = 0;
                }
            }

            if (currentChar == 'R') {
                countR++;
            } else if (previousChar == 'R') {
                if (countR > 0) {
                    if (countR > 1) {
                        path.append(countR).append("R ");
                    } else {
                        path.append("R ");
                    }
                    countR = 0;
                }
            }

            if (currentChar == 'L') {
                countL++;
            } else if (previousChar == 'L') {
                if (countL > 0) {
                    if (countL > 1) {
                        path.append(countL).append("L ");
                    } else {
                        path.append("L ");
                    }
                    countL = 0;
                }
            }

            previousChar = currentChar;
        }

        if (countF > 0) {
            if (countF > 1) {
                path.append(countF).append("F");
            } else {
                path.append("F");
            }
        }

        return path.toString().trim();
    }

    public String getCanonicalPath() {
        return canonicalPath;
    }

    public String getFactorizedPath() {
        return factorizedPath;
    }
}

class PathValidator {

    public static boolean validatePath(List<List<Integer>> maze, String path) {
        int rows = maze.size();
        int cols = maze.get(0).size();

        // Determine the starting position
        int startX = -1, startY = 0;
        for (int i = 0; i < rows; i++) {
            if (maze.get(i).get(0) == 0) {
                startX = i;
                break;
            }
        }

        if (startX == -1) {
            throw new IllegalArgumentException("No valid starting position found on the left side of the maze.");
        }

        
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Right, Down, Left, Up
        int direction = 0; // Initially facing Right (index 0)

        int x = startX, y = startY;

        for (char move : path.toCharArray()) {
            if (move == 'F') {
                int newX = x + directions[direction][0];
                int newY = y + directions[direction][1];

                if (newX < 0 || newX >= rows || newY < 0 || newY >= cols || maze.get(newX).get(newY) == 1) {
                    return false;
                }

                x = newX;
                y = newY;
            } else if (move == 'R') {
                direction = (direction + 1) % 4;
            } else if (move == 'L') {
                direction = (direction + 3) % 4; // Add 3 to simulate a left turn
            } else {
                throw new IllegalArgumentException("Invalid move character: " + move);
            }
        }

        return y == cols - 1 && maze.get(x).get(y) == 0;
    }

}

class RightHandExplorer extends Explorer {
    public RightHandExplorer(Maze maze) {
        super(maze);
    }

    @Override
    protected String generatePath(java.util.List<java.util.List<Integer>> grid, int startRow) {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Right, Down, Left, Up
        StringBuffer path = new StringBuffer();
        int leftLast = 0;
        int currentDir = 0; // initially facing right
        int currentRow = startRow;
        int currentCol = 0;

        while (currentCol < grid.get(0).size() - 1) {
            if (grid.get(currentRow + directions[(currentDir+1)%4][0]).get(currentCol + directions[(currentDir+1)%4][1]) == 0) {
                path.append(" R F");
                currentDir = (currentDir+1)%4;
                currentRow += directions[currentDir][0];
                currentCol += directions[currentDir][1];
                leftLast = 0;
            } else if (grid.get(currentRow + directions[currentDir][0]).get(currentCol + directions[currentDir][1]) == 0) {
                if (leftLast == 1) {
                    path.append(" F");
                } else {
                    path.append('F');
                }
                currentRow += directions[currentDir][0];
                currentCol += directions[currentDir][1];
                leftLast = 0;
            } else {
                if (leftLast == 0) {
                    path.append(" L");
                } else {
                    path.append('L');
                }
                currentDir = (currentDir+3)%4;
                leftLast = 1;
            }
        }

        return path.toString().strip();
    }


}

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        // logger.info("** Starting Maze Runner");

        Options options = new Options();
        options.addOption("i", "input", true, "Path to the input maze file");
        options.addOption("p", "input", true, "Path instructions to test");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("i")) {
                // logger.error("Input file not specified. Use the -i flag to specify the maze file.");
                return;
            }

            if (!cmd.hasOption("p")) {
                String inputFilePath = cmd.getOptionValue("i");
                // logger.info("Reading maze from file: " + inputFilePath);
                Maze maze = MazeReader.readFromFile(inputFilePath);

                // logger.info("Displaying maze:");
                maze.display();
                System.out.println();

                // logger.info("Exploring maze...");
                Explorer explorer = new RightHandExplorer(maze);
                System.out.println(explorer.getCanonicalPath());
                System.out.println();
                System.out.println(explorer.getFactorizedPath());
            } else {
                String inputFilePath = cmd.getOptionValue("i");
                // logger.info("Reading maze from file: " + inputFilePath);
                Maze maze = MazeReader.readFromFile(inputFilePath);

                // logger.info("Displaying maze:");
                maze.display();

                // logger.info("Testing Path...");
                String path = cmd.getOptionValue("p");
                Boolean validPath = PathValidator.validatePath(maze.getGrid(), path);
                if (validPath) {
                    System.out.println("The path " + path + " is valid!");
                } else {
                    System.out.println("The path " + path + " is not valid!");
                }
            }
        } catch (Exception e) {
            // logger.error("An error occurred: ", e);
        }

        // logger.info("** End of Maze Runner");
    }
}
