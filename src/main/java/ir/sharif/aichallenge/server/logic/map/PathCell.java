package ir.sharif.aichallenge.server.logic.map;

public class PathCell {
    private final Path path;
    private boolean reversed;
    private int moveIndex;

    private PathCell(Path path, boolean reversed, int moveIndex) {
        this.path = path;
        this.reversed = reversed;
        this.moveIndex = Math.min(path.getLength() - 1, moveIndex);
    }

    public static PathCell createPathCell(Path path, boolean reversed, int rawIndexInPath) {
        int moveIndex = Math.min(rawIndexInPath, path.getLength() - 1);
        if (reversed)
            moveIndex = path.getLength() - moveIndex - 1;
        return new PathCell(path, reversed, moveIndex);
    }


    public static PathCell createPathCell(Path path, boolean reversed, Cell targetCell) {
        int index = path.getCells().indexOf(targetCell);
        if (index == -1) throw new NullPointerException("Cell is not in the path.");

        return createPathCell(path, reversed, index);
    }


    public Path getPath() {
        return path;
    }

    public int getMoveIndex() {
        return moveIndex;
    }

    public boolean isReversed() {
        return reversed;
    }

    public Cell getCell() {
        return path.getCellAt(reversed ? path.getLength() - moveIndex - 1 : moveIndex);
    }

    public PathCell nextCell(int speed) {
        return new PathCell(this.path, this.reversed, moveIndex + speed);
    }

    @Override
    public String toString() {
        return "{" +
                "pathId=" + path.getId() +
                ", numberOfCell=" + moveIndex +
                '}';
    }
}
