package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.common.network.data.*;
import ir.sharif.aichallenge.server.logic.dto.client.ClientCell;
import ir.sharif.aichallenge.server.logic.dto.client.init.*;
import ir.sharif.aichallenge.server.logic.dto.client.turn.ClientTurnMessage;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnKing;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnUnit;
import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicMessage;
import ir.sharif.aichallenge.server.logic.dto.graphic.init.GraphicInit;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.GraphicTurn;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.TurnAttack;
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
import ir.sharif.aichallenge.server.utils.Log;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game {

    @Setter
    @Getter
    private GameState gameState;

    //region Initializations

    public void init(InitialMessage initialMessage) {
        GameStateBuilder gameStateBuilder = new GameStateBuilder();

        GameConstants gameConstants = initialMessage.getGameConstants();
        gameStateBuilder.setGameConstants(gameConstants);

        //init players
        Player[] players = new Player[4];
        for (int i = 0; i < 4; i++)
            players[i] = new Player(i, gameConstants.getMaxAP());
        gameStateBuilder.setPlayers(players);

        Map map = initMap(initialMessage.getMap(), players, gameStateBuilder);
        gameStateBuilder.setMap(map);

        initBaseUnits(initialMessage.getBaseUnits(), gameStateBuilder);

        initSpells(initialMessage.getSpells(), gameStateBuilder);

        GraphicMessage graphicMessage = new GraphicMessage();
        graphicMessage.setInit(GraphicInit.makeGraphicInit(initialMessage));
        gameStateBuilder.setGraphicMessage(graphicMessage);
    }

    private void initSpells(List<ClientSpell> spells, GameStateBuilder gameStateBuilder) {
        int numberOfSpells = spells.size();
        gameStateBuilder.setNumberOfSpells(numberOfSpells);
        for (ClientSpell clientSpell : spells)
            BaseSpell.initSpell(clientSpell);
    }

    private void initBaseUnits(List<ClientBaseUnit> baseUnits, GameStateBuilder gameStateBuilder) {
        int numberOfBaseUnits = baseUnits.size();
        gameStateBuilder.setNumberOfBaseUnits(numberOfBaseUnits);
        for (ClientBaseUnit cBU : baseUnits) {
            BaseUnit.initBaseUnits(cBU, gameStateBuilder.getGameConstants().getDamageUpgradeAddition(),
                    gameStateBuilder.getGameConstants().getRangeUpgradeAddition());
        }
    }

    private Map initMap(ClientMap clientMap, Player[] players, GameStateBuilder gameStateBuilder) {
        Map map = new Map(clientMap.getRows(), clientMap.getCols());
        List<ClientPath> clientPaths = clientMap.getPaths();

        HashMap<Cell, Integer> kingCellsWithIds = new HashMap<>();
        for (ClientBaseKing clientBaseKing : clientMap.getKings()) {
            int id = clientBaseKing.getPlayerId();
            kingCellsWithIds.put(new Cell(clientBaseKing.getCenter()), clientBaseKing.getPlayerId());
            addKing(players[id], new Cell(clientBaseKing.getCenter()),
                    clientBaseKing.getHp(), clientBaseKing.getAttack(),
                    clientBaseKing.getRange(), gameStateBuilder, map);
        }

        HashMap<Cell, Path> kingToKingPaths = new HashMap<>();
        List<Path> allPaths = new ArrayList<>(clientPaths.size() - 2);
        for (ClientPath clientPath : clientPaths) {
            Path path = new Path(clientPath.getId(), clientPath.getCells().stream().map(Cell::new).collect(Collectors.toList()));
            if (isKingToKingPath(path, kingCellsWithIds)) {
                kingToKingPaths.put(path.getFirst(), path);
                kingToKingPaths.put(path.getLast(), path.reverse());
            } else
                allPaths.add(path);
        }
        for (Path path : allPaths) {
            final Path prependedPath = kingToKingPaths.get(path.getFirst()).reverse();
            final Path appendedPath = kingToKingPaths.get(path.getLast());
            int[] kingsIndices = new int[4];
            kingsIndices[kingCellsWithIds.get(prependedPath.getFirst())] = 0;
            kingsIndices[kingCellsWithIds.get(prependedPath.getLast())] = prependedPath.getLength() - 1;
            kingsIndices[kingCellsWithIds.get(appendedPath.getFirst())] = prependedPath.getLength() + path.getLength() - 2;
            kingsIndices[kingCellsWithIds.get(appendedPath.getLast())] = prependedPath.getLength() + path.getLength() + appendedPath.getLength() - 3;
            final List<Cell> concat = Stream.of(prependedPath.getCells(), path.getCells().subList(1, path.getLength() - 1), appendedPath.getCells())
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            map.addPath(new Path(path.getId(), concat, kingsIndices));
        }

        return map;
    }

    private boolean isKingToKingPath(Path path, java.util.Map<Cell, Integer> kingCellsWithIds) {
        int startKing = kingCellsWithIds.get(path.getCellAt(0));
        int endKing = kingCellsWithIds.get(path.getCellAt(path.getLength() - 1));
        return startKing % 2 == endKing % 2;
    }

    public void addKing(Player player, Cell centerCell, int health, int damage, int range,
                        GameStateBuilder gameStateBuilder, Map map) {
        King king = new King(player, centerCell, health, damage, range);
        HashMap<Integer, Unit> unitsWithId = gameStateBuilder.getUnitsWithId();
        for (KingUnit kingUnit : king.getUnits()) {
            unitsWithId.put(kingUnit.getId(), kingUnit);
            map.putUnit(kingUnit);
        }
        gameStateBuilder.getKings().add(king);
    }

    //endregion

    //TODO: exception handling
    public void pick(List<PickInfo> messages) {
        gameState.getCurrentTurn().incrementAndGet();
        Player[] players = gameState.getPlayers();
        int numberOfBaseUnits = gameState.getNumberOfBaseUnits();

        for (PickInfo pickInfo : messages) {
            int playerId = pickInfo.getPlayerId();
            players[playerId].initDeck(pickInfo.getUnits(), numberOfBaseUnits);
        }

        for (int pId = 0; pId < 4; pId++)
            if (!players[pId].getDeckInit())
                players[pId].initDeck(new ArrayList<>(), numberOfBaseUnits);

        gameState = initializeTurn(gameState);
        gameState = checkToGiveUpgradeTokens(gameState);
        checkToGiveSpells();
        fillClientMessage();
    }

    public void turn(java.util.Map<String, List<ClientMessageInfo>> messages) {
        try {
            gameState.getCurrentTurn().incrementAndGet();

            //Ignore dead players' messages
            messages.values().forEach(list -> list.removeIf(info -> !isPlayerAlive(info.getPlayerId())));

            gameState = initializeTurn(gameState);

            gameState = applyUpgrades(messages.get(MessageTypes.UPGRADE_DAMAGE), gameState);
            gameState = applyUpgrades(messages.get(MessageTypes.UPGRADE_RANGE), gameState);

            gameState = applyPutUnits(messages.get(MessageTypes.PUT_UNIT), gameState);

            gameState = evaluateSpells(gameState);
            gameState = applySpells(messages.get(MessageTypes.CAST_SPELL), gameState);

            gameState = attack(gameState);
            gameState = move(gameState);

            gameState = evaluateUnits(gameState);

            gameState = resetPlayers(gameState);

            gameState = checkToGiveUpgradeTokens(gameState);
            checkToGiveSpells();

            fillClientMessage();
            addTurnToGraphicMessage();

            checkForGameEnd();
        } catch (Exception ex) {
            Log.e("Error", "Unhandled exception", ex);
        }
    }

    private GameState initializeTurn(GameState gameState) {
        GameStateBuilder gameStateBuilder = new GameStateBuilder(gameState);
        gameStateBuilder.setPlayedUnits(new HashSet<>());
        gameStateBuilder.setTurnCastSpells(new ArrayList<>());
        Arrays.setAll(gameStateBuilder.getClientTurnMessages(), i -> new ClientTurnMessage());
        gameStateBuilder.setDamageUpgradedUnits(new HashSet<>());
        gameStateBuilder.setRangeUpgradedUnits(new HashSet<>());
        for (Unit unit : gameStateBuilder.getUnitsWithId().values()) {
            unit.setSpeedIncrease(0);
        }
        return gameStateBuilder.toGameState();
    }

    private GameState applyUpgrades(List<ClientMessageInfo> upgradeMessages, GameState gameState) {
        if (upgradeMessages == null)
            return gameState;
        GameStateBuilder gameStateBuilder = new GameStateBuilder(gameState);

        upgradeMessages.stream().map(info -> (UpgradeInfo) info)
                .forEach(message -> {
                    try {
                        Unit unit = Objects.requireNonNull(gameStateBuilder.getUnitsWithId().get(message.getUnitId()),
                                "Unit doesn't exist. Unit id: " + message.getUnitId());   //todo if unit is kingUnit?

                        if (unit.getPlayer().getId() != message.getPlayerId())
                            throw new UpgradeOtherPlayerUnitException(message.getPlayerId(), unit.getPlayer().getId());

                        Player player = gameStateBuilder.getPlayers()[unit.getPlayer().getId()];
                        if (message.getType().equals(MessageTypes.UPGRADE_DAMAGE)) {
                            player.useUpgradeDamage();
                            unit.upgradeDamage();
                            gameStateBuilder.getClientHandler().getDamageUpgradedUnits().add(unit.getId());
                        } else {
                            player.useUpgradeRange();
                            unit.upgradeRange();
                            gameStateBuilder.getClientHandler().getRangeUpgradedUnits().add(unit.getId());
                        }
                    } catch (LogicException | NullPointerException ex) {
                        Log.i("Logic error:", ex.getMessage());
                    }
                });
        return gameStateBuilder.toGameState();
    }

    private GameState applyPutUnits(List<ClientMessageInfo> putUnitMessages, GameState gameState) {
        if (putUnitMessages == null)
            return gameState;

        GameStateBuilder gameStateBuilder = new GameStateBuilder(gameState);

        putUnitMessages.stream()
                .map(message -> (UnitPutInfo) message)
                .forEach(info -> {
                    try {
                        Player player = gameStateBuilder.getPlayers()[info.getPlayerId()];
                        BaseUnit baseUnit = Objects.requireNonNull(BaseUnit.getInstance(info.getTypeId()),
                                "Invalid unit type: " + info.getTypeId()); //TODO

                        player.checkPutUnit(baseUnit);

                        gameStateBuilder.getMap().checkValidPut(info.getPathId(), info.getPlayerId());

                        player.putUnit(baseUnit);

                        GeneralUnit generalUnit = new GeneralUnit(baseUnit, player);
                        gameStateBuilder.getMap().putUnit(generalUnit, info.getPathId());
                        gameStateBuilder.getUnitsWithId().put(generalUnit.getId(), generalUnit);
                        gameStateBuilder.getClientTurnMessages().getPlayedUnits().add(generalUnit.getId());
                    } catch (LogicException | NullPointerException ex) {
                        Log.i("Logic error:", ex.getMessage());
                    }
                });
        return gameStateBuilder.toGameState();
    }

    private GameState evaluateSpells(GameState gameState) {
        GameStateBuilder gameStateBuilder = new GameStateBuilder(gameState);
        List<Spell> removeSpells = new ArrayList<>();

        for (Spell spell : gameStateBuilder.getSpells()) {
            spell.decreaseRemainingTurns();
            if (spell.shouldRemove()) {
                spell.getCaughtUnits().forEach(unit -> unit.removeActiveSpell(spell.getId()));
                removeSpells.add(spell);
            }
        }

        gameStateBuilder.getSpells().removeAll(removeSpells);
        return gameStateBuilder.toGameState();
    }

    private GameState applySpells(List<ClientMessageInfo> castSpellMessages, GameState gameState) {
        if (castSpellMessages == null)
            return gameState;

        GameStateBuilder gameStateBuilder = new GameStateBuilder(gameState);

        for (ClientMessageInfo castMessage : castSpellMessages) {
            SpellCastInfo info = (SpellCastInfo) castMessage;
            try {
                final Player player = gameStateBuilder.getPlayers()[info.getPlayerId()];

                player.checkSpell(info.getTypeId());

                Spell spell = Objects.requireNonNull(
                        SpellFactory.createSpell(info.getTypeId(), player, info.getCell(), info.getUnitId(), gameStateBuilder.getMap().getPath(info.getPathId())),
                        "Invalid spell type: " + info.getTypeId());

                spell.checkValid(this);

                player.castSpell(info.getTypeId());

                gameStateBuilder.getSpells().add(spell);
            } catch (LogicException | NullPointerException ex) {
                Log.i("Logic error:", ex.getMessage());
            }
        }

        if (gameStateBuilder.getSpells().isEmpty())
            return gameStateBuilder.toGameState();

        Spell lastSpell = gameStateBuilder.getSpells().first();
        for (Spell spell : gameStateBuilder.getSpells()) {
            if (spell.getType() != SpellType.HP && lastSpell.getType() == SpellType.HP) {
                GameState newGameState = evaluateUnits(gameStateBuilder.toGameState()); //TODO ???
                gameStateBuilder = new GameStateBuilder(newGameState);
            }

            lastSpell = spell;
            try {
                spell.applyTo(this);
                spell.getCaughtUnits().forEach(unit -> unit.addActiveSpell(spell.getId()));
                gameStateBuilder.getClientHandler().getTurnCastSpells().add(spell.getTurnCastSpell());
            } catch (LogicException ex) {
                Log.i("Logic error:", ex.getMessage());
            }
        }
        return gameStateBuilder.toGameState();
    }

    private GameState evaluateUnits(GameState gameState) {
        GameStateBuilder gameStateBuilder = new GameStateBuilder(gameState);

        for (Iterator<java.util.Map.Entry<Integer, Unit>> iterator = gameStateBuilder.getUnitsWithId().entrySet().iterator(); iterator.hasNext(); ) {
            Unit unit = iterator.next().getValue();
            if (!unit.isAlive()) {
                gameStateBuilder.getMap().removeUnit(unit);
                iterator.remove();
            }
        }

        return gameStateBuilder.toGameState();
    }

    private GameState resetPlayers(GameState gameState) {
        GameStateBuilder gameStateBuilder = new GameStateBuilder(gameState);
        Arrays.stream(gameStateBuilder.getPlayers()).forEach(Player::reset);
        return gameStateBuilder.toGameState();
    }

    private GameState attack(GameState gameState) {
        GameStateBuilder gameStateBuilder = new GameStateBuilder(gameState);
        gameStateBuilder.getClientHandler().getCurrentAttacks() = new ArrayList<>();

        for (Unit unit : gameStateBuilder.getUnitsWithId().values()) {
            Unit targetUnit = unit.getTarget(gameStateBuilder.getMap());

            if (!(unit instanceof KingUnit)) {
                System.out.println("Unit -> " + unit.getPlayer().getId());
                if (targetUnit != null) System.out.println(targetUnit.getPlayer().getId() + "\n");
            }

            if (targetUnit == null) {
                unit.setHasAttacked(false);
                continue;
            }

            System.out.println("Here");
            gameStateBuilder.getClientHandler().getCurrentAttacks().add(TurnAttack.getTurnAttack(unit, targetUnit));
            System.out.println(gameStateBuilder.getClientHandler().currentAttacks().size());


            unit.setHasAttacked(true);
            if (unit.isMultiTarget())
                gameStateBuilder.getMap().getUnits(targetUnit.getCell())
                        .filter(unit::isTarget)
                        .forEach(target -> target.decreaseHealth(unit.getDamage()));
            else
                targetUnit.decreaseHealth(unit.getDamage());
        }
        return gameStateBuilder.toGameState();
    }

    private GameState move(GameState gameState) {
        GameStateBuilder gameStateBuilder = new GameStateBuilder(gameState);

        for (Unit unit : gameStateBuilder.getUnitsWithId().values())
            if (unit.isAlive() && !unit.hasAttacked())
                gameStateBuilder.getMap().moveUnit(unit, unit.getNextMoveCell());

        return gameStateBuilder.toGameState();
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
        isGameFinished = true;
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

    private GameState checkToGiveUpgradeTokens(GameState gameState) {
        GameStateBuilder gameStateBuilder = new GameStateBuilder(gameState);
        for (int pId = 0; pId < 4; pId++) {
            gameStateBuilder.getClientHandler().getClientTurnMessages()[pId].setGotDamageUpgrade(false);
            gameStateBuilder.getClientHandler().getClientTurnMessages()[pId].setGotRangeUpgrade(false);
        }

        if (gameStateBuilder.getCurrentTurn().get() == 0)
            return gameStateBuilder.toGameState();
        if (gameStateBuilder.getCurrentTurn().get() % gameStateBuilder.getGameConstants().getTurnsToUpgrade() != 0)
            return gameStateBuilder.toGameState();

        giveUpgradeTokens(gameStateBuilder);
        return gameStateBuilder.toGameState();
    }

    private void giveUpgradeTokens(GameStateBuilder gameStateBuilder) {
        if (gameStateBuilder.getRandomMaker().nextBoolean()) {
            giveUpgradeDamageToPlayer(0, gameStateBuilder);
            giveUpgradeRangeToPlayer(2, gameStateBuilder);
        } else {
            giveUpgradeDamageToPlayer(2, gameStateBuilder);
            giveUpgradeRangeToPlayer(0, gameStateBuilder);
        }

        if (gameStateBuilder.getRandomMaker().nextBoolean()) {
            giveUpgradeDamageToPlayer(1, gameStateBuilder);
            giveUpgradeRangeToPlayer(3, gameStateBuilder);
        } else {
            giveUpgradeDamageToPlayer(3, gameStateBuilder);
            giveUpgradeRangeToPlayer(1, gameStateBuilder);
        }
    }

    private void giveUpgradeDamageToPlayer(int playerId, GameStateBuilder gameStateBuilder) {
        gameStateBuilder.getPlayers()[playerId].addUpgradeDamageToken();
        gameStateBuilder.getClientHandler().getClientTurnMessages()[playerId].setGotDamageUpgrade(true);
    }

    private void giveUpgradeRangeToPlayer(int playerId, GameStateBuilder gameStateBuilder) {
        gameStateBuilder.getPlayers()[playerId].addUpgradeRangeToken();
        gameStateBuilder.getClientHandler().getClientTurnMessages()[playerId].setGotRangeUpgrade(true);
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

        ClientCell clientTargetCell = targetCell == null ? null : new ClientCell(targetCell);
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
                .target(targetId).targetCell(clientTargetCell)
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
            message.setCastSpells(turnCastSpells);

            message.setDeck(player.getDeckIds());
            message.setHand(player.getHandIds());
        }
    }

    private void addTurnToGraphicMessage() {
        GraphicTurn graphicTurn = graphicHandler.getGraphicTurn(this);
        graphicMessage.getTurns().add(graphicTurn);
        graphicHandler.saveGraphicLog(graphicMessage, graphicTurn);

    }


    public King getKingWithId(int id) {
        for (King king : kings)
            if (king.getMainUnit().getPlayer().getId() == id)
                return king;
        return null;
    }

    //endregion

    private boolean isPlayerAlive(int playerId) {
        return kings.get(playerId).isAlive();
    }
}