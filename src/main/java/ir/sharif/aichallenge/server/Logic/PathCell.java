package ir.sharif.aichallenge.server.Logic;

public class PathCell {
    Path path;
    int numberOfCell;

    public PathCell(Path path, int numberOfCell) {
        this.path = path;
        this.numberOfCell = numberOfCell;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getNumberOfCell() {
        return numberOfCell;
    }

    public void setNumberOfCell(int numberOfCell) {
        this.numberOfCell = numberOfCell;
    }
}
