package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.logic.dto.client.init.GameConstants;
import ir.sharif.aichallenge.server.logic.dto.client.turn.ClientTurnMessage;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnCastSpell;
import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicMessage;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.TurnAttack;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.spells.Spell;
import ir.sharif.aichallenge.server.logic.entities.units.King;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Map;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class GameStateBuilder {
    private GameConstants gameConstants;
    private int numberOfSpells;
    private int numberOfBaseUnits;
    private List<TurnAttack> currentAttacks;
    private Random randomMaker;
    private Map map;
    private SortedSet<Spell> spells;
    private Player[] players;
    private HashMap<Integer, Unit> unitsWithId;
    private ArrayList<King> kings;
    private ClientTurnMessage[] clientTurnMessages;
    private Set<Integer> damageUpgradedUnits;
    private Set<Integer> rangeUpgradedUnits;
    private Set<Integer> playedUnits;
    private List<TurnCastSpell> turnCastSpells;
    private GraphicMessage graphicMessage;
    private GraphicHandler graphicHandler;
    private AtomicInteger currentTurn;
    private boolean isGameFinished;


    public GameStateBuilder(GameState gameState) {
        this.gameConstants = gameState.getGameConstants();
        this.numberOfSpells = gameState.getNumberOfSpells();
        this.numberOfBaseUnits = gameState.getNumberOfBaseUnits();
        this.currentAttacks = gameState.getCurrentAttacks() == null ? null : new ArrayList<>(gameState.getCurrentAttacks());
        this.randomMaker = gameState.getRandomMaker();
        this.map = gameState.getMap();
        this.spells = gameState.getSpells() == null ? null : new TreeSet<>(gameState.getSpells());
        this.players = gameState.getPlayers() == null ? null : Arrays.copyOf(gameState.getPlayers(), gameState.getPlayers().length);
        this.unitsWithId = gameState.getUnitsWithId() == null ? null : new HashMap<>(gameState.getUnitsWithId());
        this.kings = gameState.getKings() == null ? null : new ArrayList<>(gameState.getKings());
        this.clientTurnMessages = gameState.getClientTurnMessages() == null ? null : Arrays.copyOf(gameState.getClientTurnMessages(), gameState.getClientTurnMessages().length);
        this.damageUpgradedUnits = gameState.getDamageUpgradedUnits() == null ? null : new HashSet<>(gameState.getDamageUpgradedUnits());
        this.rangeUpgradedUnits = gameState.getRangeUpgradedUnits() == null ? null : new HashSet<>(gameState.getRangeUpgradedUnits());
        this.playedUnits = gameState.getPlayedUnits() == null ? null : new HashSet<>(gameState.getPlayedUnits());
        this.turnCastSpells = gameState.getTurnCastSpells() == null ? null : new ArrayList<>(gameState.getTurnCastSpells());
        this.graphicMessage = gameState.getGraphicMessage();
        this.graphicHandler = gameState.getGraphicHandler();
        this.currentTurn = gameState.getCurrentTurn();
        this.isGameFinished = gameState.isGameFinished();
    }

    public GameState toGameState() {
        return new GameState(gameConstants, numberOfSpells, numberOfBaseUnits, currentAttacks, randomMaker,
                map, spells, players, unitsWithId, kings, clientTurnMessages, damageUpgradedUnits, rangeUpgradedUnits,
                playedUnits, turnCastSpells, graphicMessage, graphicHandler, currentTurn, isGameFinished);
    }

    public GameStateBuilder() {
        this(new GameState());
    }
}
