package ca.mcmaster.se2aa4.mazerunner;

// Command to explore the maze using the Template pattern.
class ExploreMazeCommand implements MazeCommand {
    private Maze maze;

    public ExploreMazeCommand(Maze maze) {
        this.maze = maze;
    }

    @Override
    public void execute() throws Exception {
        Explorer explorer = new RightHandExplorer(maze);
        explorer.explore(); // Runs the template method
        System.out.println("Canonical Path: " + explorer.getCanonicalPath());
        System.out.println("Factorized Path: " + explorer.getFactorizedPath());
    }
}