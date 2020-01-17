package ir.sharif.aichallenge.server.logic.map;

import java.util.List;

public class PathCell {
    private final Path path;
    private boolean reversed;
    private int numberOfCell;

    public PathCell(Path path, boolean reversed, int numberOfCellInPath) {
        this.path = path;
        this.reversed = reversed;
        this.numberOfCell = Math.min(numberOfCellInPath, path.getCells().size() - 1);
        if (reversed) {
            this.numberOfCell = path.getLength() - numberOfCellInPath - 1;
        }
    }


    public static PathCell createPathCell(Path path, boolean reversed, Cell targetCell) {
        int index = path.getCells().indexOf(targetCell);
        if (index == -1) throw new NullPointerException("Teleported in not valid cell in path_id");

        return new PathCell(path, reversed, index);
    }

    public Path getPath() {
        return path;
    }

    public int getNumberOfCell() {
        return numberOfCell;
    }

    public boolean isReversed() {
        return reversed;
    }

    public Cell getCell() {
        return path.getCellAt(reversed ? path.getLength() - numberOfCell - 1 : numberOfCell);
    }

    public PathCell nextCell(int speed) {
        PathCell cloned = new PathCell(this.path, this.reversed, numberOfCell + speed);
        cloned.numberOfCell = Math.min(cloned.getPath().getLength() - 1, numberOfCell + speed); //Forcing the index
        return cloned;
    }

    @Override
    public String toString() {
        return "{" +
                "pathId=" + path.getId() +
                ", numberOfCell=" + numberOfCell +
                '}';
    }
}
