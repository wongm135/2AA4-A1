package ca.mcmaster.se2aa4.mazerunner;

import java.util.List;

// PathValidator is a helper class.
class PathValidator {
    public static boolean validatePath(List<List<Integer>> maze, String path) {
        int rows = maze.size();
        int cols = maze.get(0).size();
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
        
        int[][] directions = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} }; // Right, Down, Left, Up
        int direction = 0;
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
                direction = (direction + 3) % 4;
            } else if (move == ' ') {
                continue;
            } else {
                throw new IllegalArgumentException("Invalid move character: " + move);
            }
        }
        return y == cols - 1 && maze.get(x).get(y) == 0;
    }
}