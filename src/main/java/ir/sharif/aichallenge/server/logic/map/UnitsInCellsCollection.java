package ir.sharif.aichallenge.server.logic.map;

import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import lombok.Getter;

import java.util.LinkedList;
import java.util.stream.Stream;

public class UnitsInCellsCollection {
    @Getter
    private int width;  //todo change to rows, cols
    @Getter
    private int height;
    private LinkedList<Unit>[][] unitsInCell;

    public UnitsInCellsCollection(int height, int width) {
        this.width = width;
        this.height = height;
        unitsInCell = new LinkedList[height][width];
    }

    public Stream<Unit> getUnits(int row, int col) {
        LinkedList<Unit> units;
        try {
            units = unitsInCell[row][col];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            units = null;
        }
        return units == null ? Stream.empty() : units.stream();
    }

    public Stream<Unit> getUnits(Cell cell) {
        return getUnits(cell.row, cell.col);
    }

    public boolean add(Unit unit, int row, int col) {
        if (unitsInCell[row][col] == null)
            unitsInCell[row][col] = new LinkedList<>();
        return unitsInCell[row][col].add(unit);
    }

    public boolean add(Unit unit) {
        return add(unit, unit.getCell().row, unit.getCell().col);
    }

    public boolean remove(Unit unit, int row, int col) {
        return unitsInCell[row][col].remove(unit);
    }

    public boolean remove(Unit unit) {
        return unitsInCell[unit.getCell().row][unit.getCell().col].remove(unit);
    }
}
