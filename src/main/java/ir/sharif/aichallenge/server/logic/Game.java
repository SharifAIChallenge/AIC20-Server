package ir.sharif.aichallenge.server.logic;

import com.sun.security.ntlm.Client;
import ir.sharif.aichallenge.server.common.network.data.*;
import ir.sharif.aichallenge.server.logic.dto.ClientCell;
import ir.sharif.aichallenge.server.logic.dto.init.*;
import ir.sharif.aichallenge.server.logic.entities.spells.SpellFactory;
import ir.sharif.aichallenge.server.logic.entities.units.*;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.spells.Spell;
import ir.sharif.aichallenge.server.logic.exceptions.TeleportKingException;
import ir.sharif.aichallenge.server.logic.exceptions.TeleportTooFarException;
import ir.sharif.aichallenge.server.logic.exceptions.UnitNotInMapException;
import ir.sharif.aichallenge.server.logic.exceptions.UpgradeOtherPlayerUnitException;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.Path;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import javafx.util.Pair;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Game {

    private Map map;
    private SortedSet<Spell> spells = new TreeSet<Spell>(Comparator.comparing(Spell::getPriority));
    private List<Pair<Unit, Integer>> unitsToPut = new ArrayList<>();
    private List<Unit> clonedUnitToPut = new ArrayList<>();
    private Player[] players = new Player[4];
    private HashMap<Integer, Unit> unitsWithId = new HashMap<>();
    private ArrayList<King> kings = new ArrayList<>();
    private GameConstants gameConstants;


    @Getter
    private AtomicInteger currentTurn = new AtomicInteger(0);

    public void init(InitialMessage initialMessage) {
        gameConstants = initialMessage.getGameConstants();

        //init players
        for (int i=0; i<4; i++)
            players[i] = new Player(i, gameConstants.getMaxAP(), null);

        initMap(initialMessage.getMap());

        initBaseUnits(initialMessage.getBaseUnits());



        //make initial map and paths and players.
    }

    private void initBaseUnits(List<ClientBaseUnit> baseUnits) {
        for (ClientBaseUnit cBU : baseUnits) {
            BaseUnit.initBaseUnits(cBU, gameConstants.getDamageUpgradeAddition(), gameConstants.getRangeUpgradeAddition())
        }
    }

    private void initMap(ClientMap clientMap) {
        map = new Map(clientMap.getRows(), clientMap.getCols());
        List<ClientPath> clientPaths = clientMap.getPaths();

        for (ClientPath clientPath : clientPaths)
        {
            List<Cell> cells = new ArrayList<>();
            for (ClientCell clientCell : clientPath.getCells())
                cells.add(new Cell(clientCell.getRow(), clientCell.getCol()));

            addPath(new Path(clientPath.getId(), cells));
        }

        for (ClientBaseKing clientBaseKing : clientMap.getKings()) {
            int id = clientBaseKing.getPlayerId();
            addKing(players[id], new Cell(clientBaseKing.getCenter().getRow(), clientBaseKing.getCenter().getCol()),
                    clientBaseKing.getHp(), clientBaseKing.getAttack(), clientBaseKing.getRange());
        }

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
                GeneralUnit generalUnit = new GeneralUnit(baseUnit, player);
                unitsWithId.put(generalUnit.getId(), generalUnit);
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

        for (Spell spell : spells) {
            try {
                spell.applyTo(this);
            }catch(Exception ex) {}
        }

        //TODO exceptions for teleport.
        //spells.forEach(spell -> spell.applyTo(this));
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
        GeneralUnit clonedUnit = new GeneralUnit(unit.getBaseUnit(), unit.getPlayer(),
                unit.getHealth() / rateOfHealthOfCloneUnit, unit.getDamage() / rateOfDamageCloneUnit);

        unitsWithId.put(clonedUnit.getId(), clonedUnit);
        getMap().putUnit(clonedUnit);

        return clonedUnit;
    }

    public void teleportUnit(Unit unit, PathCell targetCell) {
        if(unit instanceof KingUnit) throw new TeleportKingException();
        int index = targetCell.getNumberOfCell();
        if(index >= (targetCell.getPath().getLength() + 1)/2) throw new TeleportTooFarException();
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

    public void addKing(Player player, Cell centerCell, int health, int damage, int range) {
        King king = new King(player, centerCell, health, damage, range);
        for (KingUnit kingUnit : king.getUnits()) {
            unitsWithId.put(kingUnit.getId(), kingUnit);
            map.putUnit(kingUnit);
        }
        kings.add(king);
    }

}