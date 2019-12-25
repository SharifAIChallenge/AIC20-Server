package logic;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.entities.units.BaseUnit;
import ir.sharif.aichallenge.server.logic.entities.units.GeneralUnit;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameBasicTest {
    private static Game game;
    private static int size = 10;

    @BeforeAll
    public static void init() {

        game = new Game();
        game.initializeMap(size);

        ArrayList<Cell> p0 = new ArrayList<>();
        for (int i = size - 1; i >= 0; i--)
            p0.add(new Cell(i, i));

        ArrayList<Cell> p1 = new ArrayList<>();
        for (int i = size - 1; i >= 0; i--)
            p1.add(new Cell(i, 0));

        game.getMap().addPath(new Path(0, p0));
        game.getMap().addPath(new Path(1, p1));

    }

    @Test
    public void testAll() {
        testPutUnit1();
        testMove1();

        testAttack1();

        testRemoved();

        testPutUnit2();

        testMove2();

        testAttack2();
    }

    private void testAttack2() {
        game.turn();

        assertEquals(game.getUnitWithId().size(), 1);

        assertUnitIdsAt(2, 0, 2);
        assertUnitIdsAt(0, 0);

        Unit unit2 = game.getUnitWithId().get(2);
        assertEquals(unit2.getHealth(), 6);

        System.out.println("Tests Done");

    }

    private void testMove2() {
        game.turn(); //7,0 --- 0, 0
        game.turn();

        assertUnitIdsAt(0, 0, 1);
        assertUnitIdsAt(6, 0, 2);

        game.turn();
        game.turn();
        game.turn();
        game.turn();

        assertUnitIdsAt(0, 0, 1);
        assertUnitIdsAt(2, 0, 2);

    }

    private void testPutUnit2() {
        game.addUnit(2, 1, new GeneralUnit(2, BaseUnit.getInstance(1, 0), null));
        game.turn();

        assertUnitIdsAt(8, 0, 2);
        assertUnitIdsAt(0, 0, 1);

    }

    private void testPutUnit1() {
        game.addUnit(0, 0, new GeneralUnit(0, BaseUnit.getInstance(0, 0), null));
        game.addUnit(1, 1, new GeneralUnit(1, BaseUnit.getInstance(1, 0), null));

        game.turn();

        assertUnitIdsAt(8, 0, 1);
        assertUnitIdsAt(8, 8, 0);
    }

    private void testMove1() {
        game.turn();

        assertUnitIdsAt(7, 0, 1);
        assertUnitIdsAt(7, 7, 0);

        game.turn();
        game.turn();
        game.turn();
        game.turn();
        game.turn();

        assertUnitIdsAt(2, 0, 1);
        assertUnitIdsAt(2, 2, 0);

    }

    private void testAttack1() {

        Unit unit0 = game.getUnitWithId().get(0);
        Unit unit1 = game.getUnitWithId().get(1);

        game.turn();

        assertEquals(unit0.getHealth(), 1);
        assertEquals(unit1.getHealth(), 8);

        game.turn();

        assertEquals(unit0.getHealth(), 0);
        assertEquals(unit1.getHealth(), 1);
    }

    private void testRemoved() {
        game.turn();

        assertEquals(game.getUnitWithId().size(), 1);

        //System.out.println(g);

        assertUnitIdsAt(1, 0, 1);
        assertUnitIdsAt(1, 1);
        assertUnitIdsAt(0, 0);

    }

    private void assertUnitIdsAt(int row, int col, Integer... ids) {
        assertEquals(new HashSet<>(Arrays.asList(ids)),
                game.getMap().getUnits(row, col).map(Unit::getId).collect(Collectors.toSet()));
    }


}
