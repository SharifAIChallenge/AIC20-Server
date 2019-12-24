package ir.sharif.aichallenge.server.logic.map;

public class MapUtils {
    public static int calcManhattanDistance(Cell from, Cell to) {
        return Math.abs(from.getRow() - to.getRow()) + Math.abs(from.getCol() - to.getCol());
    }
}
