package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.logic.entities.units.ClonedUnit;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.spells.Spell;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Map;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Game {

    Map map;
    List<Spell> spells = new ArrayList<>();
    List<Pair<Unit, Integer>> unitsToPut;
    Player[] players;
    HashMap<Integer, Unit> unitWithId;

    public void init() {
        //make initial map and paths and players.
    }

    public void turn() {
        readRequestsFromClient();

        applySpells();
        applyPutUnits();

        evaluateSpells();

        attack();
        move();

        evaluateUnits();

        updateDecks();

        sendDataToClient();
    }

    private void evaluateUnits() {
        ArrayList<Unit> allUnits = getAllUnits();

        for (Unit unit : allUnits) {

            if(unit instanceof ClonedUnit)
                ((ClonedUnit) unit).decreaseRemainingTurns();

            if (!unit.isAlive()) {
                map.removeUnit(unit);
                unitWithId.remove(unit.getId());
            }
        }
    }

    private void updateDecks() {
        for (Player player : players)
            player.updateDeck();
    }

    private void sendDataToClient() {
        //network ....
    }

    private void attack() {
        //iterate over all units.
        ArrayList<Unit> allUnits = getAllUnits();

        for (Unit unit : allUnits) {
            Unit targetUnit = unit.getTarget(map);
            if (targetUnit != null)
                unit.setHasAttacked(true);
            else unit.setHasAttacked(false);
        }

        for (Unit unit : allUnits) {
            if (unit.hasAttacked()) {
                Unit targetUnit = unit.getTarget(map);
                targetUnit.decreaseHealth(unit.getDamage());
            }
        }
    }

    private void move() {
        ArrayList<Unit> allUnits = getAllUnits();

        for (Unit unit : allUnits) {
            if (unit.isAlive() && !unit.hasAttacked())
                map.moveUnit(unit, unit.getNextMoveCell());
        }

    }

    private void evaluateSpells() {
        List<Spell> removeSpells = new ArrayList<>();

        for (Spell spell : spells) {
            spell.decreaseRemainingTurns();
            if (spell.shouldRemove()) removeSpells.add(spell);
        }

        for (Spell spell : removeSpells)
            spells.remove(spell);
    }

    private void applyPutUnits() {
        for (Pair<Unit, Integer> X : unitsToPut) {
            map.putUnit(X.getKey(), X.getValue());
        }
    }

    private void applySpells() {
        for (Spell spell : spells) {
            spell.applyTo(map);
        }
    }

    private void readRequestsFromClient() {
        //network ....
    }

    private ArrayList<Unit> getAllUnits() {
        Collection<Unit> units = unitWithId.values();
        return new ArrayList<>(units);
    }
}