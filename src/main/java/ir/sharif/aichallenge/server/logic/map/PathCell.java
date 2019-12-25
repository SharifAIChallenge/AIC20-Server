package ir.sharif.aichallenge.server.logic.map;

public class PathCell {
    Path path;
    int numberOfCell;

    public PathCell(Path path, int numberOfCell) {
        this.path = path;
        this.numberOfCell = Math.min(numberOfCell, path.getCells().size() - 1);
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

    public Cell getCell() {
        return path.getCellAt(numberOfCell);
    }

    public PathCell nextCell() {
        return nextCell(1);
    }

    public PathCell nextCell(int speed) {
        return new PathCell(this.path, numberOfCell + speed);
    }

    @Override
    public String toString() {
        return "PathCell{" +
                "pathId=" + path.getId() +
                ", numberOfCell=" + numberOfCell +
                '}';
    }
}
