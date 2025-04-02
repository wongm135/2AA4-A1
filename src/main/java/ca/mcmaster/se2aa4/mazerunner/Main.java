package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Main class acts as the Command Invoker.
public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("i", "input", true, "Path to the input maze file");
        options.addOption("p", "path", true, "Path instructions to test");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption("i")) {
                System.err.println("Input file not specified. Use the -i flag to specify the maze file.");
                return;
            }

            MazeCommand command;
            String inputFilePath = cmd.getOptionValue("i");

            Maze maze = MazeReader.readFromFile(inputFilePath);
            maze.display();

            if (cmd.hasOption("p")) {
                String path = cmd.getOptionValue("p");
                command = new ValidatePathCommand(maze, path);
            } else {
                command = new ExploreMazeCommand(maze);
            }
            // Execute the chosen command
            command.execute();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
