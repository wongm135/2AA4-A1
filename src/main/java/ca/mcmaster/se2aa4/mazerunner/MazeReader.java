package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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