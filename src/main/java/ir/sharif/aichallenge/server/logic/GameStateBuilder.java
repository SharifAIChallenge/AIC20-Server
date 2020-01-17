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
    private final GameConstants gameConstants;
    private final int numberOfSpells;
    private final int numberOfBaseUnits;
    private final Random randomMaker;
    private final Map map;
    private final SortedSet<Spell> spells;
    private final Player[] players;
    private final HashMap<Integer, Unit> unitsWithId;
    private final ArrayList<King> kings;
    private final GraphicMessage graphicMessage;
    private final GraphicHandler graphicHandler;
    private final AtomicInteger currentTurn;
    private final boolean isGameFinished;
    private final ClientHandler clientHandler;


    public GameStateBuilder(GameState gameState) {
        this.gameConstants = gameState.getGameConstants();
        this.numberOfSpells = gameState.getNumberOfSpells();
        this.numberOfBaseUnits = gameState.getNumberOfBaseUnits();
        this.randomMaker = gameState.getRandomMaker();
        this.map = gameState.getMap();
        this.spells = gameState.getSpells();
        this.players = gameState.getPlayers();
        this.unitsWithId = gameState.getUnitsWithId();
        this.kings = gameState.getKings();
        this.graphicMessage = gameState.getGraphicMessage();
        this.graphicHandler = gameState.getGraphicHandler();
        this.currentTurn = gameState.getCurrentTurn();
        this.isGameFinished = gameState.isGameFinished();
        this.clientHandler = gameState.getClientHandler();
    }

    public GameState toGameState() {
        return new GameState(gameConstants, numberOfSpells, numberOfBaseUnits, randomMaker,
                map, spells, players, unitsWithId, kings, graphicMessage, graphicHandler, currentTurn,
                isGameFinished, clientHandler);
    }

    public GameStateBuilder() {
        this(new GameState());
    }
}
