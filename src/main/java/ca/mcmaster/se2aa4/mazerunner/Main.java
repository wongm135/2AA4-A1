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

    public String explore(Maze maze) {
        // Retrieve the 2D grid representation
        var grid = maze.getGrid();

        // Find the starting position
        int startRow = findStartRow(grid);

        // Generate the path
        String path = generatePath(grid, startRow);

        // Print the computed path
        System.out.println(path);

        return path;
    }

    private int findStartRow(java.util.List<java.util.List<Integer>> grid) {
        for (int row = 0; row < grid.size(); row++) {
            if (grid.get(row).get(0) == 0) {
                return row;
            }
        }
        throw new IllegalArgumentException("No valid start found in the maze!");
    }

    // initial path generation for mvp
    // assumes the maze is a direct path, (no dead ends)
    private String generatePath(java.util.List<java.util.List<Integer>> grid, int startRow) {
        StringBuilder path = new StringBuilder();
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
}

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("** Starting Maze Runner");

        Options options = new Options();
        options.addOption("i", "input", true, "Path to the input maze file");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("i")) {
                logger.error("Input file not specified. Use the -i flag to specify the maze file.");
                return;
            }

            String inputFilePath = cmd.getOptionValue("i");
            logger.info("Reading maze from file: " + inputFilePath);
            Maze maze = MazeReader.readFromFile(inputFilePath);

            logger.info("Displaying maze:");
            maze.display();

            logger.info("Exploring maze...");
            Explorer explorer = new Explorer();
            explorer.explore(maze);

        } catch (Exception e) {
            logger.error("An error occurred: ", e);
        }

        logger.info("** End of Maze Runner");
    }
}
