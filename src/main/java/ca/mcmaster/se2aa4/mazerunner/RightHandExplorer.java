package ca.mcmaster.se2aa4.mazerunner;

import java.util.List;

// Concrete implementation using the right-hand rule.
class RightHandExplorer extends Explorer {

    public RightHandExplorer(Maze maze) {
        super(maze);
    }

    @Override
    protected String generatePath() {
        int[][] directions = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} }; // Right, Down, Left, Up
        StringBuffer path = new StringBuffer();
        int currentDir = 0; // Initially facing right
        int currentRow = startRow;
        int currentCol = 0;
        boolean leftLast = false;

        List<List<Integer>> grid = maze.getGrid();
        while (currentCol < grid.get(0).size() - 1) {
            // Try turning right relative to current direction.
            int rightDir = (currentDir + 1) % 4;
            if (grid.get(currentRow + directions[rightDir][0])
                   .get(currentCol + directions[rightDir][1]) == 0) {
                path.append("RF");
                currentDir = rightDir;
                currentRow += directions[currentDir][0];
                currentCol += directions[currentDir][1];
                leftLast = false;
            }
            // Move forward if possible.
            else if (grid.get(currentRow + directions[currentDir][0])
                          .get(currentCol + directions[currentDir][1]) == 0) {
                if (leftLast) {
                    path.append("F");
                } else {
                    path.append("F");
                }
                currentRow += directions[currentDir][0];
                currentCol += directions[currentDir][1];
                leftLast = false;
            }
            // Otherwise, turn left.
            else {
                if (!leftLast) {
                    path.append("L");
                } else {
                    path.append("L");
                }
                currentDir = (currentDir + 3) % 4;
                leftLast = true;
            }
        }
        return path.toString().strip();
    }
}