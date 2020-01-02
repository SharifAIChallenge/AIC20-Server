package ir.sharif.aichallenge.server.logic.map;

import ir.sharif.aichallenge.server.logic.entities.TargetType;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Map {
    HashMap<Integer, Path> paths;

    UnitsInCellsCollection unitsInCell;
    ArrayList<Unit> kings;

    public Map(int height, int width) {
        unitsInCell = new UnitsInCellsCollection(height, width);
        paths = new HashMap<>();
    }

    public int getHeight() {
        return unitsInCell.getHeight();
    }

    public int getWidth() {
        return unitsInCell.getWidth();
    }

    public void addPath(Path path) {
        this.paths.put(path.getId(), path);
    }

    public Path getPath(int pathId) {
        return this.paths.get(pathId);
    }

    public void putUnit(Unit unit, int pathId) {
        final Path path = this.paths.get(pathId);
        unit.setPosition(new PathCell(path, unit.getPlayer().getTeam() == 0, 0));
        unitsInCell.add(unit);
    }

    public void putUnit(Unit unit) {
        unitsInCell.add(unit);
    }


    public void moveUnit(Unit unit, PathCell target) {
        unitsInCell.remove(unit);
        unit.setPosition(target);
        unitsInCell.add(unit);
    }

    public void removeUnit(Unit unit) {
        unitsInCell.remove(unit);
    }

    public Stream<Unit> getUnits(Cell cell) {
        return getUnits(cell.getRow(), cell.getCol());
    }

    public Stream<Unit> getUnits(int row, int col) {
        return unitsInCell.getUnits(row, col);
    }

    public Stream<Unit> getUnitsInArea(int centerRow, int centerCol, int range) {
        return getUnitsInAreaInclusive(centerRow - range, centerCol - range,
                centerRow + range, centerCol + range);
    }

    public Stream<Unit> getUnitsInAreaInclusive(int startRow, int startCol, int endRow, int endCol) {
        return IntStream.rangeClosed(startRow, endRow).boxed()
                .flatMap(r -> IntStream.rangeClosed(startCol, endCol).boxed()
                        .flatMap(c -> unitsInCell.getUnits(r, c)));
    }

    public Stream<Unit> getUnitsInManhattanRange(Cell cell, int range) {
        return getUnitsInManhattanRange(cell.row, cell.col, range);
    }

    public Stream<Unit> getUnitsInManhattanRange(int centerRow, int centerCol, int range) {
        return IntStream.rangeClosed(0, range).boxed()
                .flatMap(i -> getUnitsWithManhattanDistance(centerRow, centerCol, range));
    }

    public Stream<Unit> getUnitsWithManhattanDistance(Cell cell, int distance) {
        return getUnitsWithManhattanDistance(cell.getRow(), cell.getCol(), distance);
    }

    public Stream<Unit> getUnitsWithManhattanDistance(int centerRow, int centerCol, int distance) {
        return IntStream.rangeClosed(-distance, distance).boxed()
                .flatMap(j ->
                        Stream.concat(getUnits(centerRow + j, centerCol + distance - Math.abs(j)),
                                getUnits(centerRow + j, centerCol - distance + Math.abs(j))));
    }
}
