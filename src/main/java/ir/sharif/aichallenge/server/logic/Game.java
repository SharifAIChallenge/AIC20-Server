package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.Spell;
import ir.sharif.aichallenge.server.logic.entities.Unit;
import ir.sharif.aichallenge.server.logic.map.Map;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

    Map map;
    List<Spell> spells = new ArrayList<>();
    List<Pair<Unit, Integer>> unitsToPut;
    Player[] players;

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
        Stream<Unit> streamUnits = map.getUnitsInArea(0, 0, map.getHeight(), map.getWidth());
        ArrayList<Unit> allUnits = streamUnits.collect(Collectors.toCollection(ArrayList::new));

        for (Unit unit : allUnits)
            if(!unit.isPresent())
                map.removeUnit(unit);

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

        Stream<Unit> streamUnits = map.getUnitsInArea(0, 0, map.getHeight(), map.getWidth());
        ArrayList<Unit> allUnits = streamUnits.collect(Collectors.toCollection(ArrayList::new));

        for (Unit unit : allUnits) {
            Unit targetUnit = unit.getTarget(map);
            if (targetUnit != null)
                unit.setHasAttacked(true);
            else unit.setHasAttacked(false);
        }

        for (Unit unit : allUnits) {
            if (unit.getHasAttacked()) {
                Unit targetUnit = unit.getTarget(map);
                targetUnit.decreaseHealth(unit.getDamage());
            }
        }
    }

    private void move() {
        Stream<Unit> streamUnits = map.getUnitsInArea(0, 0, map.getHeight(), map.getWidth());
        ArrayList<Unit> allUnits = streamUnits.collect(Collectors.toCollection(ArrayList::new));

        for (Unit unit : allUnits) {
            if(unit.isPresent() && !unit.getHasAttacked())
                map.moveUnit(unit, unit.nextCell());
        }

    }

    private void evaluateSpells() {
        List<Spell> removeSpells = new ArrayList<>();

        for (Spell spell : spells) {
            spell.decreaseTime();
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
}