package ir.sharif.aichallenge.server.logic.map;

import ir.sharif.aichallenge.server.logic.entities.Unit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UnitsInCellsCollection {
    private LinkedList<Unit>[][] unitsInCell;

    public UnitsInCellsCollection(int width, int height) {
        unitsInCell = new LinkedList[height][width];
    }

    public List<Unit> getUnits(int row, int col) {
        return Collections.unmodifiableList(unitsInCell[row][col]);
    }

    public List<Unit> getUnits(Cell cell) {
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
