package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.common.network.data.*;
import ir.sharif.aichallenge.server.logic.dto.init.InitialMessage;
import ir.sharif.aichallenge.server.logic.entities.spells.SpellFactory;
import ir.sharif.aichallenge.server.logic.entities.units.BaseUnit;
import ir.sharif.aichallenge.server.logic.entities.units.ClonedUnit;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.spells.Spell;
import ir.sharif.aichallenge.server.logic.entities.units.GeneralUnit;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.exceptions.UnitNotInMapException;
import ir.sharif.aichallenge.server.logic.exceptions.UpgradeOtherPlayerUnitException;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.Path;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import javafx.util.Pair;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

    Map map;
    SortedSet<Spell> spells = new TreeSet<Spell>(Comparator.comparing(Spell::getPriority));
    List<Pair<Unit, Integer>> unitsToPut = new ArrayList<>();
    List<Unit> clonedUnitToPut = new ArrayList<>();

    Player[] players;
    HashMap<Integer, Unit> unitsWithId = new HashMap<>();

    private int numberOfUnits = 0;

    @Getter
    private AtomicInteger currentTurn = new AtomicInteger(0);

    public void init(InitialMessage initialMessage) {
        //make initial map and paths and players.
    }

    public void pick(java.util.Map<String, List<ClientMessageInfo>> messages) {
        //TODO: to be implemented
        currentTurn.incrementAndGet();
    }

    public void turn(java.util.Map<String, List<ClientMessageInfo>> messages) {

        applyRangeUpgrades(messages.get(MessageTypes.UPGRADE_RANGE));
        applyDamageUpgrades(messages.get(MessageTypes.UPGRADE_DAMAGE));
        applySpells(messages.get(MessageTypes.CAST_SPELL));
        applyPutUnits(messages.get(MessageTypes.PUT_UNIT));

        evaluateSpells();

        attack();
        move();

        evaluateUnits();

        updateDecks();

        sendDataToClient();

        currentTurn.incrementAndGet();
    }

    private void evaluateUnits() {
        ArrayList<Unit> allUnits = getAllUnits();

        for (Unit unit : allUnits) {

            if (unit instanceof ClonedUnit)
                ((ClonedUnit) unit).decreaseRemainingTurns();

            if (!unit.isAlive()) {
                map.removeUnit(unit);
                unitsWithId.remove(unit.getId());
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
        if (players == null) return;
        for (Player player : players)
            player.updateHand();
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

    private void applyDamageUpgrades(List<ClientMessageInfo> clientMessageInfos) {
        for (ClientMessageInfo msg : clientMessageInfos) {
            try {
                Unit unit = unitsWithId.get(((DamageUpgradeInfo)msg).getUnitId());
                if(unit == null) throw new UnitNotInMapException();
                if(unit.getPlayer().getId() != msg.getPlayerId()) throw new UpgradeOtherPlayerUnitException();
                Player player = players[unit.getPlayer().getId()];
                player.useUpgradeDamage();
                unit.upgradeDamage();
            }catch(Exception ex) {

            }
        }
    }


    private void applyRangeUpgrades(List<ClientMessageInfo> clientMessageInfos) {
        for (ClientMessageInfo msg : clientMessageInfos) {
            try {
                Unit unit = unitsWithId.get(((RangeUpgradeInfo)msg).getUnitId());
                if(unit == null) throw new UnitNotInMapException();
                if(unit.getPlayer().getId() != msg.getPlayerId()) throw new UpgradeOtherPlayerUnitException();
                Player player = players[unit.getPlayer().getId()];
                player.useUpgradeRange();
                unit.upgradeRange();
            }catch (Exception ex) { }
        }
    }



    private void applyPutUnits(List<ClientMessageInfo> putUnitMessages) {
        putUnitMessages.stream().map(message -> (PutUnitInfo) message).forEach(info -> {
            try {
                Player player = players[info.getPathId()];
                BaseUnit baseUnit = BaseUnit.getInstance(info.getTypeId());
                player.putUnit(baseUnit);
                int id = numberOfUnits++;
                unitsWithId.put(id, new GeneralUnit(id, baseUnit, player));
            } catch (Exception ex) {

            }
        });
    }

    private void applySpells(List<ClientMessageInfo> castSpellMessages) {
        castSpellMessages.stream().map(info -> (CastSpellInfo) info).forEach(info -> {
            try {
                final Player player = players[info.getPlayerId()];
                player.castSpell(info.getTypeId());
                spells.add(SpellFactory.createSpell(info.getTypeId(), player, info.getCell(), info.getUnitId(), map.getPath(info.getPathId())));
            } catch (Exception ex) {
            }
        });

        spells.forEach(spell -> spell.applyTo(this));
    }

    private void readRequestsFromClient() {
        //network ....
    }

    private ArrayList<Unit> getAllUnits() {
        Collection<Unit> units = unitsWithId.values();
        return new ArrayList<>(units);
    }

    public void addUnit(Integer pathId, Unit unit) {
        unitsWithId.put(unit.getId(), unit);
        unitsToPut.add(new Pair<>(unit, pathId));
    }

    public GeneralUnit cloneUnit(Unit unit, int rateOfHealthOfCloneUnit, int rateOfDamageCloneUnit) {
        GeneralUnit clonedUnit = new GeneralUnit(numberOfUnits, unit.getBaseUnit(), unit.getPlayer(),
                unit.getHealth() / rateOfHealthOfCloneUnit, unit.getDamage() / rateOfDamageCloneUnit);
        unitsWithId.put(clonedUnit.getId(), clonedUnit);
        clonedUnitToPut.add(clonedUnit);
        numberOfUnits++;
        return clonedUnit;
    }

    public void teleportUnit(Unit unit, PathCell targetCell) {
        getMap().moveUnit(unit, targetCell);
    }

    public Map getMap() {
        return map;
    }

    public void initializeMap(int size) {
        map = new Map(size, size);
    }

    public HashMap<Integer, Unit> getUnitsWithId() {
        return unitsWithId;
    }

    public Unit getUnitById(int id) {
        return this.unitsWithId.get(id);
    }


    public void addPath(Path path) {
        map.addPath(path);
    }
}