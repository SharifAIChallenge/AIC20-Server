package ir.sharif.aichallenge.server.logic.map;

import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.exceptions.InvalidPathPutUnitException;
import ir.sharif.aichallenge.server.logic.exceptions.LogicException;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Map {
    public HashMap<Integer, Path> paths;
    private UnitsInCellsCollection unitsInCell;

    public Map(int width, int height) {
        unitsInCell = new UnitsInCellsCollection(width, height);
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
        boolean isReversed = path.shouldReverseForTeam(unit.getPlayer().getTeam());
        final int index = path.getIndexForKing(unit.getPlayer().getId());
        unit.setPosition(PathCell.createPathCell(path, isReversed, index));
        unitsInCell.add(unit);
    }

    public void checkValidPut(int pathId, int playeerId) throws LogicException {
        if (!this.paths.containsKey(pathId))
            throw new InvalidPathPutUnitException(pathId, playeerId);
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
        return getUnitsInManhattanRange(cell.getRow(), cell.getCol(), range);
    }

    public Stream<Unit> getUnitsInManhattanRange(int centerRow, int centerCol, int range) {
        return IntStream.rangeClosed(0, range).boxed()
                .flatMap(i -> getUnitsWithManhattanDistance(centerRow, centerCol, i));
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
