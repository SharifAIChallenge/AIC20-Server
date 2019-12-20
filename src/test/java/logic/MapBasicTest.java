package logic;

import com.sun.rowset.internal.Row;
import ir.sharif.aichallenge.server.logic.entities.BaseUnit;
import ir.sharif.aichallenge.server.logic.entities.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.Path;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MapBasicTest {

    private static Map map;
    private static HashMap<Integer, Unit> units;

    @BeforeAll
    public static void init() {
        final int size = 10;
        map = new Map(size, size);
        units = new HashMap<>();
        map.addPath(new Path(0, IntStream.range(0, size).mapToObj(i -> new Cell(i, i)).collect(Collectors.toList())));
        map.addPath(new Path(1, IntStream.range(0, size).mapToObj(i -> new Cell(i, 0)).collect(Collectors.toList())));
        map.addPath(new Path(2, IntStream.range(0, size).mapToObj(i -> new Cell(i, size - 1)).collect(Collectors.toList())));
    }

    @Test
    @Order(0)
    public void testPutUnit() {
        map.putUnit(addUnit(new Unit(0, BaseUnit.getInstance(0, 0), null)), 0);
        map.putUnit(addUnit(new Unit(1, BaseUnit.getInstance(1, 0), null)), 1);
        map.putUnit(addUnit(new Unit(2, BaseUnit.getInstance(0, 0), null)), 2);

        assertUnitIdsAt(0, 0, 0, 1);
        assertUnitIdsAt(0, 9, 2);
    }

    @Test
    @Order(1)
    public void testMoveUnit() {
        map.moveUnit(units.get(0), units.get(0).nextCell());
        map.moveUnit(units.get(1), units.get(1).nextCell());
        map.moveUnit(units.get(2), units.get(2).nextCell());

        assertUnitIdsAt(1, 1, 0);
        assertUnitIdsAt(1, 0, 1);
        assertUnitIdsAt(1, 9, 2);
    }

    public void assertUnitIdsAt(int row, int col, Integer... ids) {
        assertEquals(map.getUnits(row, col).map(Unit::getUnitId).collect(Collectors.toSet()),
                new HashSet<>(Arrays.asList(ids)));
    }

    private Unit addUnit(Unit unit) {
        units.put(unit.getUnitId(), unit);
        return unit;
    }
}
