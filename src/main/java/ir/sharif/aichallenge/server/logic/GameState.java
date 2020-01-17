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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public final class GameState {
    private final GameConstants gameConstants;
    private final int numberOfSpells;
    private final int numberOfBaseUnits;

    private final Random randomMaker;
    private final Map map;
    private final SortedSet<Spell> spells;
    private final Player[] players;
    private final HashMap<Integer, Unit> unitsWithId;
    private final ArrayList<King> kings;
    private final ClientTurnMessage[] clientTurnMessages;

    private final List<TurnCastSpell> turnCastSpells;
    private final GraphicMessage graphicMessage;
    private final GraphicHandler graphicHandler;
    private final AtomicInteger currentTurn;
    private final boolean isGameFinished;
    private final ClientHandler clientHandler;


    public GameState(GameConstants gameConstants, int numberOfSpells, int numberOfBaseUnits,
                     List<TurnAttack> currentAttacks, Random randomMaker, Map map, SortedSet<Spell> spells,
                     Player[] players, HashMap<Integer, Unit> unitsWithId, ArrayList<King> kings,
                     ClientTurnMessage[] clientTurnMessages, Set<Integer> damageUpgradedUnits,
                     Set<Integer> rangeUpgradedUnits, Set<Integer> playedUnits, List<TurnCastSpell> turnCastSpells,
                     GraphicMessage graphicMessage, GraphicHandler graphicHandler, AtomicInteger currentTurn,
                     boolean isGameFinished) {
        this.gameConstants = gameConstants;
        this.numberOfSpells = numberOfSpells;
        this.numberOfBaseUnits = numberOfBaseUnits;
        this.currentAttacks = currentAttacks;
        this.randomMaker = randomMaker;
        this.map = map;
        this.spells = spells;
        this.players = players;
        this.unitsWithId = unitsWithId;
        this.kings = kings;
        this.clientTurnMessages = clientTurnMessages;
        this.damageUpgradedUnits = damageUpgradedUnits;
        this.rangeUpgradedUnits = rangeUpgradedUnits;
        this.playedUnits = playedUnits;
        this.turnCastSpells = turnCastSpells;
        this.graphicMessage = graphicMessage;
        this.graphicHandler = graphicHandler;
        this.currentTurn = currentTurn;
        this.isGameFinished = isGameFinished;
    }

    public GameState() {
        this.gameConstants = null;
        this.numberOfSpells = 0;
        this.numberOfBaseUnits = 0;
        this.currentAttacks = new ArrayList<>();
        this.randomMaker = new Random();
        this.map = null;
        this.spells = new TreeSet<>(Comparator.comparing(Spell::getPriority).thenComparing(Spell::getId));
        this.players = new Player[4];
        this.unitsWithId = new HashMap<>();
        this.kings = new ArrayList<>();
        this.clientTurnMessages = new ClientTurnMessage[4];
        this.damageUpgradedUnits = null;
        this.rangeUpgradedUnits = null;
        this.playedUnits = new HashSet<>();
        this.turnCastSpells = new ArrayList<>();
        this.graphicMessage = new GraphicMessage();
        this.graphicHandler = new GraphicHandler();
        this.currentTurn = null;
        this.isGameFinished = false;
    }
}
