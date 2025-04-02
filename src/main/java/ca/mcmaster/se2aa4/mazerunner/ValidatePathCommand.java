package ca.mcmaster.se2aa4.mazerunner;

// Command to validate a provided path.
class ValidatePathCommand implements MazeCommand {
    private Maze maze;
    private String path;

    public ValidatePathCommand(Maze maze, String path) {
        this.maze = maze;
        this.path = path;
    }

    @Override
    public void execute() throws Exception {
        boolean valid = PathValidator.validatePath(maze.getGrid(), path);
        if (valid) {
            System.out.println("The path " + path + " is valid!");
        } else {
            System.out.println("The path " + path + " is not valid!");
        }
    }
}