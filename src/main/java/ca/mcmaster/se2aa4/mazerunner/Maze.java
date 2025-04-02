package ca.mcmaster.se2aa4.mazerunner;

import java.util.List;
import java.util.ArrayList;

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
        List<List<Integer>> gridCopy = new ArrayList<>();

        for (List<Integer> row : grid) {
            List<Integer> rowData = new ArrayList<>();
            for (int x : row) {
                rowData.add(x);
            }
            gridCopy.add(rowData);
        }

        return gridCopy; // returns a copy of the grid (prevents leaky abstraction)
    }

    public void display() {
        for (List<Integer> row : grid) {
            for (int cell : row) {
                if (cell == 1) {
                    System.out.print("WALL ");
                } else {
                    System.out.print("PASS ");
                }
            }
            System.out.println();
        }
    }
}