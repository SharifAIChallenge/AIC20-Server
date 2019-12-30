package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.common.network.data.*;
import ir.sharif.aichallenge.server.logic.dto.ClientCell;
import ir.sharif.aichallenge.server.logic.dto.init.*;
import ir.sharif.aichallenge.server.logic.dto.turn.ClientTurnMessage;
import ir.sharif.aichallenge.server.logic.dto.turn.TurnKing;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.spells.BaseSpell;
import ir.sharif.aichallenge.server.logic.entities.spells.Spell;
import ir.sharif.aichallenge.server.logic.entities.units.*;
import ir.sharif.aichallenge.server.logic.exceptions.*;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.Path;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import javafx.util.Pair;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {

    private int numberOfSpells;
    private int numberOfBaseUnits;

    private Map map;
    private SortedSet<Spell> spells = new TreeSet<Spell>(Comparator.comparing(Spell::getPriority));
    private List<Pair<Unit, Integer>> unitsToPut = new ArrayList<>();
    private List<Unit> clonedUnitToPut = new ArrayList<>();
    private Player[] players = new Player[4];
    private HashMap<Integer, Unit> unitsWithId = new HashMap<>();
    private ArrayList<King> kings = new ArrayList<>();
    private GameConstants gameConstants;
    @Getter
    private ClientTurnMessage[] clientTurnMessages = new ClientTurnMessage[4];  //todo set each turn
    private Set<Integer> damageUpgradedUnits;
    private Set<Integer> rangeUpgradedUnits;
    private Set<Integer> hastedUnits;
    private Set<Integer> cloneUnits;
    private java.util.Map<Unit, List<Spell>> activeSpellsOnUnits;
    private java.util.Map<Integer, List<Unit>> affectedUnits;


    @Getter
    private AtomicInteger currentTurn = new AtomicInteger(0);

    public void init(InitialMessage initialMessage) {
        gameConstants = initialMessage.getGameConstants();

        //init players
        for (int i = 0; i < 4; i++)
            players[i] = new Player(i, gameConstants.getMaxAP());

        initMap(initialMessage.getMap());

        initBaseUnits(initialMessage.getBaseUnits());

        initSpells(initialMessage.getSpells());

    }

    private void initSpells(List<ClientSpell> spells) {
        numberOfSpells = spells.size();
        for (ClientSpell clientSpell : spells)
            BaseSpell.initSpell(clientSpell);
    }

    private void initBaseUnits(List<ClientBaseUnit> baseUnits) {
        numberOfBaseUnits = baseUnits.size();
        for (ClientBaseUnit cBU : baseUnits) {
            BaseUnit.initBaseUnits(cBU, gameConstants.getDamageUpgradeAddition(), gameConstants.getRangeUpgradeAddition());
        }
    }

    private void initMap(ClientMap clientMap) {
        map = new Map(clientMap.getRows(), clientMap.getCols());
        List<ClientPath> clientPaths = clientMap.getPaths();

        for (ClientPath clientPath : clientPaths) {
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

    public void pick(List<PickInfo> messages) {

        for (PickInfo pickInfo : messages) {
            int playerId = pickInfo.getPlayerId();
            players[playerId].initDeck(pickInfo.getUnits(), numberOfBaseUnits);
        }

        currentTurn.incrementAndGet();


    }

    public void turn(java.util.Map<String, List<ClientMessageInfo>> messages) {
        initializeTurn();

        applyRangeUpgrades(messages.get(MessageTypes.UPGRADE_RANGE));
        applyDamageUpgrades(messages.get(MessageTypes.UPGRADE_DAMAGE));

        applySpells(messages.get(MessageTypes.CAST_SPELL)); //todo

        applyPutUnits(messages.get(MessageTypes.PUT_UNIT)); //todo isn't put before spells?

        evaluateSpells();

        attack();
        move();
        evaluateUnits();

        resetPlayers();

        checkToGiveUpgradeTokens();
        checkToGiveSpells();

        fillClientMessage();

        currentTurn.incrementAndGet();
    }

    private void fillClientMessage() {

        List<TurnKing> turnKings = new ArrayList<>();
        for (int pId=0; pId<4; pId++) {
            int health = kings.get(pId).getHealthComponent().getHealth();
            turnKings.add(new TurnKing(pId, health > 0, health));
        }

        for (int pId=0; pId<4; pId++) {
            int friendId = pId ^ 2;

            clientTurnMessages[pId].setKings(turnKings);

            clientTurnMessages[pId].setAvailableRangeUpgrades(players[pId].getNumberOfRangeUpgrades());
            clientTurnMessages[pId].setAvailableDamageUpgrades(players[pId].getNumberOfDamageUpgrades());

            clientTurnMessages[pId].setRemainingAP(players[pId].getAp());

            clientTurnMessages[pId].setMySpells(players[pId].getAvailableSpellIds());
            clientTurnMessages[pId].setFriendSpells(players[friendId].getAvailableSpellIds());


            clientTurnMessages[pId].setCurrTurn(currentTurn.get());

            clientTurnMessages[pId].setDeck(players[pId].getDeckIds());
            clientTurnMessages[pId].setHand(players[pId].getHandIds());

        }
    }

    private int getRandom(int L, int R) { //[L, R)
        int rnd = (int)(Math.random() * (R - L)) + L;
        return rnd;
    }

    private void checkToGiveSpells() {
        if(currentTurn.get() % gameConstants.getTurnsToSpell() != 0) return ;

        for (int pId=0; pId<4; pId ++) {
            clientTurnMessages[pId].setReceivedSpell(-1);
            clientTurnMessages[pId].setFriendReceivedSpell(-1);
        }
        giveSpells();
    }

    private void giveSpellToPlayer(int playerId, int type) {
        players[playerId].addSpell(type);
        clientTurnMessages[playerId].setReceivedSpell(type);
        clientTurnMessages[playerId ^ 2].setFriendReceivedSpell(type);
    }

    private void giveSpells() {
        int type1 = getRandom(0, numberOfSpells);
        int type2 = getRandom(0, numberOfSpells);

        int rnd = getRandom(0, 2);

        if(rnd == 0) {
            giveSpellToPlayer(0, type1);
            giveSpellToPlayer(2, type2);
        }else {
            giveSpellToPlayer(0, type2);
            giveSpellToPlayer(2, type1);
        }

        rnd = getRandom(0, 2);
        if(rnd == 0) {
            giveSpellToPlayer(1, type1);
            giveSpellToPlayer(3, type2);
        }else {
            giveSpellToPlayer(1, type2);
            giveSpellToPlayer(3, type1);
        }

    }

    private void checkToGiveUpgradeTokens() {
        if (currentTurn.get() % gameConstants.getTurnsToUpgrade() != 0) return;
        for (int pId=0; pId<4; pId++) {
            clientTurnMessages[pId].setGotDamageUpgrade(false);
            clientTurnMessages[pId].setGotRangeUpgrade(false);
        }
        giveUpgradeTokens();

    }

    private void giveUpgradeDamageToPlayer(int playerId) {
        players[playerId].addUpgradeDamageToken();
        clientTurnMessages[playerId].setGotDamageUpgrade(true);
    }

    private void giveUpgradeRangeToPlayer(int playerId) {
        players[playerId].addUpgradeRangeToken();
        clientTurnMessages[playerId].setGotRangeUpgrade(true);
    }

    private void giveUpgradeTokens() {
        int rnd = getRandom(0, 2);

        if (rnd == 0) {
            giveUpgradeDamageToPlayer(0);
            giveUpgradeRangeToPlayer(2);
        } else {
            giveUpgradeDamageToPlayer(2);
            giveUpgradeRangeToPlayer(0);
        }

        rnd = getRandom(0, 2);
        if (rnd == 0) {
            giveUpgradeDamageToPlayer(1);
            giveUpgradeRangeToPlayer(3);
        } else {
            giveUpgradeDamageToPlayer(3);
            giveUpgradeRangeToPlayer(1);
        }
    }


    private void applyNonHPSpells(List<ClientMessageInfo> clientMessageInfos) {

    }

    private void applyHPSpells(List<ClientMessageInfo> clientMessageInfos) {

    }

    private void initializeTurn() {
        for (int i = 0; i < 4; i++) {
            clientTurnMessages[i] = new ClientTurnMessage();
        }
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

    private void resetPlayers() {
        if (players == null) return;
        for (Player player : players)
            player.reset();
    }

    private void attack() {
        //iterate over all units.
        ArrayList<Unit> allUnits = getAllUnits();

        for (Unit unit : allUnits) {
            Unit targetUnit = unit.getTarget(map);
            if (targetUnit != null) {
                unit.setHasAttacked(true);

                if (unit.isMultiTarget())
                    map.getUnits(targetUnit.getCell())
                            .filter(unit::isTarget)
                            .forEach(target -> target.decreaseHealth(unit.getDamage()));
                else
                    targetUnit.decreaseHealth(unit.getDamage());
            } else
                unit.setHasAttacked(false);
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
                Unit unit = unitsWithId.get(((DamageUpgradeInfo) msg).getUnitId());
                if (unit == null) throw new UnitNotInMapException();
                if (unit.getPlayer().getId() != msg.getPlayerId()) throw new UpgradeOtherPlayerUnitException();
                Player player = players[unit.getPlayer().getId()];
                player.useUpgradeDamage();
                unit.upgradeDamage();
                damageUpgradedUnits.add(unit.getId());
            } catch (Exception ex) {

            }
        }
    }


    private void applyRangeUpgrades(List<ClientMessageInfo> clientMessageInfos) {
        for (ClientMessageInfo msg : clientMessageInfos) {
            try {
                Unit unit = unitsWithId.get(((RangeUpgradeInfo) msg).getUnitId());
                if (unit == null) throw new UnitNotInMapException();
                if (unit.getPlayer().getId() != msg.getPlayerId()) throw new UpgradeOtherPlayerUnitException();
                Player player = players[unit.getPlayer().getId()];
                player.useUpgradeRange();
                unit.upgradeRange();
                rangeUpgradedUnits.add(unit.getId());
            } catch (Exception ex) {    //todo catch all possible exceptions or remove the exception classes
            }
        }
    }


    private void applyPutUnits(List<ClientMessageInfo> putUnitMessages) {
        putUnitMessages.stream().map(message -> (UnitPutInfo) message).forEach(info -> {
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
//        applyHPSpells(messages.get(MessageTypes.CAST_SPELL).stream().filter());
        evaluateUnits();
//        applyNonHPSpells(messages.get(MessageTypes.CAST_SPELL));

        /*castSpellMessages.stream().map(info -> (SpellCastInfo) info).forEach(info -> {
            try {
                final Player player = players[info.getPlayerId()];
                player.castSpell(info.getTypeId());
                Spell spell = SpellFactory.createSpell(
                        info.getTypeId(), player, info.getCell(), info.getUnitId(), map.getPath(info.getPathId()));
                spells.add(spell);

            } catch (Exception ex) {
            }
        });

        for (Spell spell : spells) {
            try {
                spell.applyTo(this);
            } catch (Exception ex) {
            }
        }*/

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
        clonedUnit.setCloned();

        unitsWithId.put(clonedUnit.getId(), clonedUnit);
        getMap().putUnit(clonedUnit);

        return clonedUnit;
    }

    public void teleportUnit(Unit unit, PathCell targetCell) {
        //TODO clean code
        if(unit == null)
            throw new NullPointerException();

        if(!unit.isAlive())
            throw new NotAliveUnitException();

        if (unit instanceof KingUnit) throw new TeleportKingException();
        int index = targetCell.getNumberOfCell();
        if (index >= (targetCell.getPath().getLength() + 1) / 2) throw new TeleportTooFarException();
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