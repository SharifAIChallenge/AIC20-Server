package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.logic.entities.units.ClonedUnit;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.spells.Spell;
import ir.sharif.aichallenge.server.logic.entities.units.GeneralUnit;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.Path;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Game {

    Map map;
    List<Spell> spells = new ArrayList<>();
    List<Pair<Unit, Integer>> unitsToPut = new ArrayList<>();
    List<Unit> clonedUnitToPut = new ArrayList<>();

    Player[] players;
    HashMap<Integer, Unit> unitWithId = new HashMap<>();

    private int numberOfUnits = 0;

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

            if (unit instanceof ClonedUnit)
                ((ClonedUnit) unit).decreaseRemainingTurns();

            if (!unit.isAlive()) {
                map.removeUnit(unit);
                unitWithId.remove(unit.getId());
            }
        }
    }

    private void upgradeUnitRange(Unit unit) {
        unit.upgradeRange();
    }

    private void upgradeUnitDamage(Unit unit) {
        unit.upgradeDamage();
    }

    private void updateDecks() {
        if(players == null) return ;
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
        unitsToPut.clear();

        for (Unit unit : clonedUnitToPut) {
            map.putUnit(unit);
        }
        clonedUnitToPut.clear();

    }

    private void applySpells() {
        for (Spell spell : spells) {
            spell.applyTo(this);
        }
    }

    private void readRequestsFromClient() {
        //network ....
    }

    private ArrayList<Unit> getAllUnits() {
        Collection<Unit> units = unitWithId.values();
        return new ArrayList<>(units);
    }

    public void addUnit(Integer pathId, Unit unit) {
        unitWithId.put(unit.getId(), unit);
        unitsToPut.add(new Pair<>(unit, pathId));
    }

    public GeneralUnit cloneUnit(Unit unit, int rateOfHealthOfCloneUnit, int rateOfDamageCloneUnit) {
        GeneralUnit clonedUnit = new GeneralUnit(numberOfUnits, unit.getBaseUnit(), unit.getPlayer(),
                unit.getHealth() / rateOfHealthOfCloneUnit, unit.getDamage() / rateOfDamageCloneUnit);
        unitWithId.put(clonedUnit.getId(), clonedUnit);
        clonedUnitToPut.add(clonedUnit);
        numberOfUnits++;
        return clonedUnit;
    }


    public Map getMap() {
        return map;
    }

    public void initializeMap(int size) {
        map = new Map(size, size);
    }

    public HashMap<Integer, Unit> getUnitWithId(){
        return unitWithId;
    }


    public void addPath(Path path) {
        map.addPath(path);
    }
}