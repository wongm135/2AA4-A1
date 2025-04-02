package ca.mcmaster.se2aa4.mazerunner;

// Command Pattern: Define a command interface for maze operations.
interface MazeCommand {
    void execute() throws Exception;
}