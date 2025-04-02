package ca.mcmaster.se2aa4.mazerunner;

import java.util.List;

// Template Pattern: Abstract Explorer defines the exploration algorithm.
abstract class Explorer {
    protected Maze maze;
    protected int startRow;
    protected String canonicalPath;
    protected String factorizedPath;

    public Explorer(Maze maze) {
        this.maze = maze;
        this.startRow = findStart();
        this.canonicalPath = generatePath();
        this.factorizedPath = factorizePath(this.canonicalPath);
    }

    // Template method that defines the overall steps.
    public final void explore() {
        canonicalPath = generatePath();
        factorizedPath = factorizePath(canonicalPath);
    }

    // Common implementation to find the starting point.
    protected int findStart() {
        List<List<Integer>> grid = maze.getGrid();
        for (int row = 0; row < grid.size(); row++) {
            if (grid.get(row).get(0) == 0) {
                return row;
            }
        }
        throw new IllegalArgumentException("No valid start found in the maze!");
    }

    // Abstract step to be implemented by concrete explorers.
    protected abstract String generatePath();

    // Common step to factorize the generated path.
    protected String factorizePath(String path) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        char prev = '\0';

        for (char current : path.toCharArray()) {
            if (current == ' ') continue;
            if (current == prev) {
                count++;
            } else {
                if (prev != '\0') {
                    appendInstruction(result, prev, count);
                }
                prev = current;
                count = 1;
            }
        }
        appendInstruction(result, prev, count);
        return result.toString().trim();
    }

    private void appendInstruction(StringBuilder sb, char instruction, int count) {
        if (count > 1) {
            sb.append(count).append(instruction).append(" ");
        } else {
            sb.append(instruction).append(" ");
        }
    }

    public String getCanonicalPath() {
        return canonicalPath;
    }

    public String getFactorizedPath() {
        return factorizedPath;
    }
}