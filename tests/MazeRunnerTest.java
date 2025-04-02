package ca.mcmaster.se2aa4.mazerunner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import ca.mcmaster.se2aa4.mazerunner.*;

public class MazeRunnerTest {

    @Test
    public void testMazeReader() throws IOException {
        Maze maze = MazeReader.readFromFile("./examples/small.maz.txt");
        assertNotNull(maze, "Maze should not be null");
        assertFalse(maze.getGrid().isEmpty(), "Maze grid should not be empty");
    }

    @Test
    public void testMazeDisplayOutput() throws IOException {
        Maze maze = MazeReader.readFromFile("./examples/small.maz.txt");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        maze.display();

        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue(output.contains("WALL") || output.contains("PASS"),
            "Display output should contain 'WALL' or 'PASS'");
    }

    @Test
    public void testExplorerCanonicalPathNotEmpty() throws IOException {
        Maze maze = MazeReader.readFromFile("./examples/medium.maz.txt");
        Explorer explorer = new RightHandExplorer(maze);
        String canonicalPath = explorer.getCanonicalPath();
        assertNotNull(canonicalPath, "Canonical path should not be null");
        assertFalse(canonicalPath.trim().isEmpty(), "Canonical path should not be empty");
    }

    @Test
    public void testExplorerFactorizedPathNotEmpty() throws IOException {
        Maze maze = MazeReader.readFromFile("./examples/medium.maz.txt");
        Explorer explorer = new RightHandExplorer(maze);
        String factorizedPath = explorer.getFactorizedPath();
        assertNotNull(factorizedPath, "Factorized path should not be null");
        assertFalse(factorizedPath.trim().isEmpty(), "Factorized path should not be empty");
    }

    @Test
    public void testPathValidatorValid() throws IOException {
        Maze maze = MazeReader.readFromFile("./examples/direct.maz.txt");
        boolean valid = PathValidator.validatePath(maze.getGrid(), "FRFFLFFFRFLFRFLFF");
        assertTrue(valid, "The provided path should be valid for maze_easy.txt");
    }

    @Test
    public void testPathValidatorInvalid() throws IOException {
        Maze maze = MazeReader.readFromFile("./examples/large.maz.txt");
        boolean valid = PathValidator.validatePath(maze.getGrid(), "FFFF");
        assertFalse(valid, "The provided path should be invalid for large.maz.txt");
    }

    @Test
    public void testMazeNoValidStart() {
        Maze maze = new Maze();
        maze.addRow("###");
        maze.addRow("###");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RightHandExplorer(maze);
        });
        assertTrue(exception.getMessage().contains("No valid start"),
            "Exception message should indicate no valid start found");
    }

    @Test
    public void testRightHandExplorerPathGeneration() throws IOException {
        Maze maze = MazeReader.readFromFile("./examples/large.maz.txt");
        Explorer explorer = new RightHandExplorer(maze);
        String canonicalPath = explorer.getCanonicalPath();
        assertTrue(canonicalPath.contains("F"), "Canonical path should contain 'F'");
    }

    @Test
    public void testMainWithoutPathFlag() {
        String[] args = {"-i", "./examples/small.maz.txt"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Main.main(args);

        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue(output.contains("Canonical Path") || output.contains("PASS"),
            "Output should contain exploration information");
    }

    @Test
    public void testMainWithPathFlag() {
        String[] args = {"-i", "./examples/medium.maz.txt", "-p", "FFRFF"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Main.main(args);

        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue(output.contains("valid") || output.contains("not valid"),
            "Output should contain a validation message");
    }
}
