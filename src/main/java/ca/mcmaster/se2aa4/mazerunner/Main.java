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
    private final List<String> grid;

    public Maze() {
        this.grid = new ArrayList<>();
    }

    public void addRow(String row) {
        grid.add(row);
    }

    public List<String> getGrid() {
        return grid;
    }

    public void display() {
        for (String row : grid) {
            for (int idx = 0; idx < row.length(); idx++) {
                char cell = row.charAt(idx);
                if (cell == '#') {
                    System.out.print("WALL ");
                } else if (cell == ' ') {
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
    public void explore(Maze maze) {
        // Placeholder for pathfinding logic
        System.out.println("PATH NOT COMPUTED");
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
