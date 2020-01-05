package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.common.network.data.*;
import ir.sharif.aichallenge.server.engine.core.GameServer;
import ir.sharif.aichallenge.server.logic.dto.client.ClientCell;
import ir.sharif.aichallenge.server.logic.dto.client.init.*;
import ir.sharif.aichallenge.server.logic.dto.client.turn.ClientTurnMessage;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnCastSpell;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnKing;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnUnit;
import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicMessage;
import ir.sharif.aichallenge.server.logic.dto.graphic.init.GraphicInit;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.*;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.spells.BaseSpell;
import ir.sharif.aichallenge.server.logic.entities.spells.Spell;
import ir.sharif.aichallenge.server.logic.entities.spells.SpellFactory;
import ir.sharif.aichallenge.server.logic.entities.spells.SpellType;
import ir.sharif.aichallenge.server.logic.entities.units.*;
import ir.sharif.aichallenge.server.logic.exceptions.*;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.Path;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game {

    private GameConstants gameConstants;
    private int numberOfSpells;
    private int numberOfBaseUnits;

    @Getter
    private List<TurnAttack> currentAttacks = new ArrayList<>();

    private Random randomMaker = new Random();


    @Getter
    private Map map;
    @Getter
    private SortedSet<Spell> spells = new TreeSet<>(Comparator.comparing(Spell::getPriority));

    @Getter
    private Player[] players = new Player[4];
    @Getter
    private HashMap<Integer, Unit> unitsWithId = new HashMap<>();
    private ArrayList<King> kings = new ArrayList<>();
    @Getter
    private ClientTurnMessage[] clientTurnMessages = new ClientTurnMessage[4];  //todo set each turn
    private Set<Integer> damageUpgradedUnits;
    private Set<Integer> rangeUpgradedUnits;
    private Set<Integer> playedUnits = new HashSet<>();
    private List<TurnCastSpell> turnCastSpells = new ArrayList<>();


    private GraphicMessage graphicMessage = new GraphicMessage();
    private GraphicHandler graphicHandler = new GraphicHandler();

    @Getter
    @Setter
    private AtomicInteger currentTurn;


    public static void main(String[] args) throws InterruptedException {
        AtomicInteger currentTurn = new AtomicInteger(0);

        GameServer gameServer = new GameServer(new GameHandler(currentTurn), args, currentTurn);
        gameServer.start();
        gameServer.waitForFinish();
    }

    //region Initializations

    public void init(InitialMessage initialMessage) {
        gameConstants = initialMessage.getGameConstants();

        //init players
        for (int i = 0; i < 4; i++)
            players[i] = new Player(i, gameConstants.getMaxAP());

        initMap(initialMessage.getMap());

        initBaseUnits(initialMessage.getBaseUnits());

        initSpells(initialMessage.getSpells());

        graphicMessage.setInit(GraphicInit.makeGraphicInit(initialMessage));
    }

    public void initializeMap(int size) {
        map = new Map(size, size);
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

            map.addPath(new Path(clientPath.getId(), cells));
        }

        for (ClientBaseKing clientBaseKing : clientMap.getKings()) {
            int id = clientBaseKing.getPlayerId();
            addKing(players[id], new Cell(clientBaseKing.getCenter().getRow(), clientBaseKing.getCenter().getCol()),
                    clientBaseKing.getHp(), clientBaseKing.getAttack(), clientBaseKing.getRange());
        }

    }

    public void addKing(Player player, Cell centerCell, int health, int damage, int range) {
        King king = new King(player, centerCell, health, damage, range);
        for (KingUnit kingUnit : king.getUnits()) {
            unitsWithId.put(kingUnit.getId(), kingUnit);
            map.putUnit(kingUnit);
        }
        kings.add(king);
    }

    //endregion

    public void pick(List<PickInfo> messages) {
        for (PickInfo pickInfo : messages) {
            int playerId = pickInfo.getPlayerId();
            players[playerId].initDeck(pickInfo.getUnits(), numberOfBaseUnits);
        }

        initializeTurn();
        checkToGiveUpgradeTokens();
        checkToGiveSpells();
        fillClientMessage();

        currentTurn.incrementAndGet();
    }

    public void turn(java.util.Map<String, List<ClientMessageInfo>> messages) {
        initializeTurn();

        applyUpgrades(messages.get(MessageTypes.UPGRADE_DAMAGE));
        applyUpgrades(messages.get(MessageTypes.UPGRADE_RANGE));

        applyPutUnits(messages.get(MessageTypes.PUT_UNIT));

        evaluateSpells();
        applySpells(messages.get(MessageTypes.CAST_SPELL));

        attack();
        move();
        evaluateUnits();

        resetPlayers();

        checkToGiveUpgradeTokens();
        checkToGiveSpells();

        fillClientMessage();
        addTurnToGraphicMessage();


        if (!checkForGameEnd())
            currentTurn.incrementAndGet();
    }

    private void initializeTurn() {
        playedUnits.clear();
        turnCastSpells.clear();
        Arrays.setAll(clientTurnMessages, i -> new ClientTurnMessage());
        damageUpgradedUnits = new HashSet<>();
        rangeUpgradedUnits = new HashSet<>();
    }

    //TODO upgrade exception handling.
    private void applyUpgrades(List<ClientMessageInfo> upgradeMessages) {
        if (upgradeMessages == null)
            return;

        upgradeMessages.stream().map(info -> (UpgradeInfo) info)
                .forEach(message -> {
                    try {
                        Unit unit = unitsWithId.get(message.getUnitId());   //todo if unit is kingUnit?

                        if (unit == null) throw new UnitNotInMapException();
                        if (unit.getPlayer().getId() != message.getPlayerId())
                            throw new UpgradeOtherPlayerUnitException();
                        Player player = players[unit.getPlayer().getId()];
                        if (message.getType().equals(MessageTypes.UPGRADE_DAMAGE)) {
                            player.useUpgradeDamage();
                            unit.upgradeDamage();
                            damageUpgradedUnits.add(unit.getId());
                        } else {
                            player.useUpgradeRange();
                            unit.upgradeRange();
                            rangeUpgradedUnits.add(unit.getId());
                        }
                    } catch (LogicException ex) {
                        System.out.println("logic exception!");
                    }
                });
    }

    private void applyPutUnits(List<ClientMessageInfo> putUnitMessages) {
        if (putUnitMessages == null)
            return;

        putUnitMessages.stream()
                .map(message -> (UnitPutInfo) message)
                .forEach(info -> {
                    try {
                        Player player = players[info.getPlayerId()];
                        BaseUnit baseUnit = BaseUnit.getInstance(info.getTypeId()); //TODO
                        player.putUnit(baseUnit);
                        GeneralUnit generalUnit = new GeneralUnit(baseUnit, player);
                        unitsWithId.put(generalUnit.getId(), generalUnit);

                        playedUnits.add(generalUnit.getId());
                    } catch (Exception ex) {
                    }
                });
    }

    private void evaluateSpells() {
        List<Spell> removeSpells = new ArrayList<>();

        for (Spell spell : spells) {
            spell.decreaseRemainingTurns();
            if (spell.shouldRemove()) {
                spell.getCaughtUnits().forEach(unit -> unit.removeActiveSpell(spell.getId()));
                removeSpells.add(spell);
            }
        }

        spells.removeAll(removeSpells);
    }

    private void applySpells(List<ClientMessageInfo> castSpellMessages) {
        if (castSpellMessages == null)
            return;

        castSpellMessages.stream().map(info -> (SpellCastInfo) info).forEach(info -> {
            try {
                final Player player = players[info.getPlayerId()];
                if (player.castSpell(info.getTypeId())) {
                    Spell spell = SpellFactory.createSpell(
                            info.getTypeId(), player, info.getCell(), info.getUnitId(), map.getPath(info.getPathId()));
                    spells.add(spell);
                }
            } catch (Exception ex) {
            }
        });

        if (spells.isEmpty())
            return;

        Spell lastSpell = spells.first();
        for (Spell spell : spells) {
            if (spell.getType() != SpellType.HP && lastSpell.getType() == SpellType.HP)
                evaluateUnits();
            lastSpell = spell;
            try {
                spell.applyTo(this);
                spell.getCaughtUnits().forEach(unit -> unit.addActiveSpell(spell.getId()));
                turnCastSpells.add(spell.getTurnCastSpell());
            } catch (Exception ex) {
            }
        }
    }

    private void evaluateUnits() {
        for (Iterator<java.util.Map.Entry<Integer, Unit>> iterator = unitsWithId.entrySet().iterator(); iterator.hasNext(); ) {
            Unit unit = iterator.next().getValue();
            if (!unit.isAlive()) {
                map.removeUnit(unit);
                iterator.remove();
            }
        }
    }

    private void resetPlayers() {
        Arrays.stream(players).forEach(Player::reset);
    }

    private void attack() {

        currentAttacks.clear();

        for (Unit unit : unitsWithId.values()) {
            Unit targetUnit = unit.getTarget(map);
            if (targetUnit == null) {
                unit.setHasAttacked(false);
                continue;
            }

            currentAttacks.add(TurnKing.getTurnKing(unit, targetUnit));

            unit.setHasAttacked(true);
            if (unit.isMultiTarget())
                map.getUnits(targetUnit.getCell())
                        .filter(unit::isTarget)
                        .forEach(target -> target.decreaseHealth(unit.getDamage()));
            else
                targetUnit.decreaseHealth(unit.getDamage());
        }
    }

    private void move() {
        for (Unit unit : unitsWithId.values())
            if (unit.isAlive() && !unit.hasAttacked())
                map.moveUnit(unit, unit.getNextMoveCell());
    }

    public GeneralUnit cloneUnit(Unit unit, int rateOfHealthOfCloneUnit, int rateOfDamageCloneUnit) {

        GeneralUnit clonedUnit = new GeneralUnit(unit.getBaseUnit(), unit.getPlayer(),
                unit.getHealth() / rateOfHealthOfCloneUnit, unit.getDamage() / rateOfDamageCloneUnit);
        clonedUnit.setDuplicate();

        unitsWithId.put(clonedUnit.getId(), clonedUnit);
        getMap().putUnit(clonedUnit);

        return clonedUnit;
    }

    public void teleportUnit(Unit unit, PathCell targetCell) {
        Objects.requireNonNull(unit);

        if (!unit.isAlive())    //Impossible
            throw new NotAliveUnitException();

        if (unit instanceof KingUnit) throw new TeleportKingException();
        int index = targetCell.getNumberOfCell();
        if (index >= (targetCell.getPath().getLength() + 1) / 2) throw new TeleportTooFarException();
        getMap().moveUnit(unit, targetCell);
    }

    public Unit getUnitById(int id) {
        return this.unitsWithId.get(id);
    }

    private boolean checkForGameEnd() {
        if (currentTurn.get() >= gameConstants.getMaxTurns())
            finishAndGiveScores();
        else if (!kings.get(0).isAlive() && !kings.get(2).isAlive() ||
                !kings.get(1).isAlive() && !kings.get(3).isAlive())
            finishAndGiveScores();
        else
            return false;
        return true;
    }

    private void finishAndGiveScores() {
        int[] scores = new int[4];

        int[] healthsSum = new int[2];
        int[] healths = kings.stream().mapToInt(King::getHealth).map(h -> Math.max(0, h)).toArray();
        for (int i = 0; i < 4; i++)
            healthsSum[i % 2] += healths[i];

        //TODO: move scores to game constants
        if (healthsSum[0] == healthsSum[1]) {
            for (int i = 0; i < 2; i++) {
                scores[i] = healths[i] > healths[i + 2] ? 6 : healths[i] < healths[i + 2] ? 4 : 5;
                scores[i + 2] = 10 - scores[i];
            }
        } else {
            int winningTeam = healthsSum[0] > healthsSum[1] ? 0 : 1;
            for (int i = 0; i < 2; i++) {
                if (i == winningTeam)
                    scores[i] = healths[i] > healths[i + 2] ? 8 : healths[i] < healths[i + 2] ? 6 : 7;
                else
                    scores[i] = healths[i] > healths[i + 2] ? 4 : healths[i] < healths[i + 2] ? 2 : 3;
                scores[i + 2] = (i == winningTeam ? 14 : 6) - scores[i];
            }
        }

        finishGame(scores);
    }

    private void finishGame(int[] scores) {
        //TODO: end loop, shutdown, ...
    }

    //region Token Givings

    private void checkToGiveSpells() {
        for (int pId = 0; pId < 4; pId++) {
            clientTurnMessages[pId].setReceivedSpell(-1);
            clientTurnMessages[pId].setFriendReceivedSpell(-1);
        }

        if (currentTurn.get() == 0)
            return;
        if (currentTurn.get() % gameConstants.getTurnsToSpell() != 0) return;

        giveSpells();
    }

    private void giveSpellToPlayer(int playerId, int type) {
        players[playerId].addSpell(type);
        clientTurnMessages[playerId].setReceivedSpell(type);
        clientTurnMessages[playerId ^ 2].setFriendReceivedSpell(type);
    }

    private void giveSpells() {
        int type1 = randomMaker.nextInt(numberOfSpells);
        int type2 = randomMaker.nextInt(numberOfSpells);

        if (randomMaker.nextBoolean()) {
            giveSpellToPlayer(0, type1);
            giveSpellToPlayer(2, type2);
        } else {
            giveSpellToPlayer(0, type2);
            giveSpellToPlayer(2, type1);
        }

        if (randomMaker.nextBoolean()) {
            giveSpellToPlayer(1, type1);
            giveSpellToPlayer(3, type2);
        } else {
            giveSpellToPlayer(1, type2);
            giveSpellToPlayer(3, type1);
        }

    }

    private void checkToGiveUpgradeTokens() {
        for (int pId = 0; pId < 4; pId++) {
            clientTurnMessages[pId].setGotDamageUpgrade(false);
            clientTurnMessages[pId].setGotRangeUpgrade(false);
        }

        if (currentTurn.get() == 0)
            return;
        if (currentTurn.get() % gameConstants.getTurnsToUpgrade() != 0)
            return;

        giveUpgradeTokens();
    }

    private void giveUpgradeTokens() {
        if (randomMaker.nextBoolean()) {
            giveUpgradeDamageToPlayer(0);
            giveUpgradeRangeToPlayer(2);
        } else {
            giveUpgradeDamageToPlayer(2);
            giveUpgradeRangeToPlayer(0);
        }

        if (randomMaker.nextBoolean()) {
            giveUpgradeDamageToPlayer(1);
            giveUpgradeRangeToPlayer(3);
        } else {
            giveUpgradeDamageToPlayer(3);
            giveUpgradeRangeToPlayer(1);
        }
    }

    private void giveUpgradeDamageToPlayer(int playerId) {
        players[playerId].addUpgradeDamageToken();
        clientTurnMessages[playerId].setGotDamageUpgrade(true);
    }

    private void giveUpgradeRangeToPlayer(int playerId) {
        players[playerId].addUpgradeRangeToken();
        clientTurnMessages[playerId].setGotRangeUpgrade(true);
    }

    //endregion

    //region Client Message

    private TurnUnit buildTurnUnit(Unit unit) {
        int pathId = -1;

        int targetId = -1;
        Cell targetCell = null;
        if (unit.getTargetUnit() != null) {
            if (unit.getTargetUnit() instanceof KingUnit)
                targetId = unit.getPlayer().getId();
            else
                targetId = unit.getTargetUnit().getId();
            targetCell = unit.getTargetUnit().getCell();
        }


        return TurnUnit.builder().unitId(unit.getId()).playerId(unit.getPlayer().getId()).typeId(unit.getBaseUnit().getType()).
                pathId(pathId).cell(new ClientCell(unit.getCell()))
                .hp(unit.getHealth())
                .attack(unit.getDamage()).damageLevel(unit.getDamageLevel())
                .wasDamageUpgraded(damageUpgradedUnits.contains(unit.getId()))
                .range(unit.getRange()).rangeLevel(unit.getRangeLevel())
                .wasRangeUpgraded(rangeUpgradedUnits.contains(unit.getId()))
                .isDuplicate(unit.isDuplicate())
                .isHasted(unit.getSpeedIncrease() > 0)
                .affectedSpells(unit.getAffectedSpells())
                .target(targetId).targetCell(new ClientCell(targetCell))
                .wasPlayedThisTurn(playedUnits.contains(unit.getId())).build();
    }

    private void bindPathId(TurnUnit tunit, int sendToId, Unit unit) {
        tunit.setPathId(unit.getPlayer().isAlly(sendToId) ? unit.getPath().getId() : -1);
    }

    private void fillClientMessage() {

        List<TurnKing> turnKings = IntStream.range(0, 4).boxed()
                .map(pId -> {
                    final King king = kings.get(pId); //TODO
                    int health = king.getHealth();
                    final Unit targetUnit = king.getMainUnit().getTargetUnit();
                    return new TurnKing(pId, health > 0, health, targetUnit == null ? -1 : targetUnit.getId());
                })
                .collect(Collectors.toList());

        final List<Unit> units = new ArrayList<>(unitsWithId.values())
                .stream().filter(unit -> !(unit instanceof KingUnit)).collect(Collectors.toList());
        final List<TurnUnit> turnUnits = units.stream()
                .map(this::buildTurnUnit)
                .collect(Collectors.toList());

        for (int pId = 0; pId < 4; pId++) {
            int friendId = pId ^ 2;

            final ClientTurnMessage message = clientTurnMessages[pId];
            final Player player = players[pId];

            for (int i = 0; i < units.size(); i++)
                bindPathId(turnUnits.get(i), pId, units.get(i));
            message.setUnits(turnUnits);
            message.setCurrTurn(currentTurn.get());

            message.setKings(turnKings);

            message.setAvailableRangeUpgrades(player.getNumberOfRangeUpgrades());
            message.setAvailableDamageUpgrades(player.getNumberOfDamageUpgrades());

            message.setRemainingAP(player.getAp());

            message.setMySpells(player.getAvailableSpellIds());
            message.setFriendSpells(players[friendId].getAvailableSpellIds());

            message.setDeck(player.getDeckIds());
            message.setHand(player.getHandIds());
        }
    }

    private void addTurnToGraphicMessage() {
        GraphicTurn graphicTurn = graphicHandler.getGraphicTurn(this);
        graphicMessage.getTurns().add(graphicTurn);
    }


    public King getKingWithId(int id) {
        for (King king : kings)
            if (king.getMainUnit().getPlayer().getId() == id)
                return king;
        return null;
    }

    //endregion
}