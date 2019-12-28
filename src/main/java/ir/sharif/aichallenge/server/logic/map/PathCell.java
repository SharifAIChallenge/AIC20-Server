package ir.sharif.aichallenge.server.logic.map;

public class PathCell {
    private final Path path;
    private boolean reversed;
    private int numberOfCell;

    public PathCell(Path path, boolean reversed, int numberOfCell) {
        this.path = path;
        this.reversed = reversed;
        this.numberOfCell = Math.min(numberOfCell, path.getCells().size() - 1);
    }

    public Path getPath() {
        return path;
    }

    public int getNumberOfCell() {
        return numberOfCell;
    }

    public boolean getReversed(){
        return reversed;
    }

    public Cell getCell() {
        return path.getCellAt(reversed ? path.getLength() - numberOfCell - 1: numberOfCell);
    }

    public PathCell nextCell() {
        return nextCell(1);
    }

    public PathCell nextCell(int speed) {
        return new PathCell(this.path, this.reversed, numberOfCell + speed);
    }

    @Override
    public String toString() {
        return "{" +
                "pathId=" + path.getId() +
                ", numberOfCell=" + numberOfCell +
                '}';
    }
}
