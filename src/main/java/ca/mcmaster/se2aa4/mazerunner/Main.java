package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
            logger.info("**** Reading the maze from file: " + inputFilePath);

            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                for (int idx = 0; idx < line.length(); idx++) {
                    if (line.charAt(idx) == '#') {
                        System.out.print("WALL ");
                    } else if (line.charAt(idx) == ' ') {
                        System.out.print("PASS ");
                    }
                }
                System.out.print(System.lineSeparator());
            }
        } catch(Exception e) {
            logger.error("/!\\ An error has occured /!\\");
        }
        logger.info("**** Computing path");
        System.out.println("PATH NOT COMPUTED");
        logger.info("** End of MazeRunner");
    }
}
