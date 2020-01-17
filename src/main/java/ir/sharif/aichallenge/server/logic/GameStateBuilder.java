package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.logic.dto.client.init.GameConstants;
import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicMessage;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.spells.Spell;
import ir.sharif.aichallenge.server.logic.entities.units.King;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Map;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class GameStateBuilder {
    private GameConstants gameConstants;
    private int numberOfSpells;
    private int numberOfBaseUnits;
    private Random randomMaker;
    private Map map;
    private SortedSet<Spell> spells;
    private Player[] players;
    private HashMap<Integer, Unit> unitsWithId;
    private ArrayList<King> kings;
    private GraphicMessage graphicMessage;
    private GraphicHandler graphicHandler;
    private AtomicInteger currentTurn;
    private boolean isGameFinished;
    private ClientHandler clientHandler;


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
