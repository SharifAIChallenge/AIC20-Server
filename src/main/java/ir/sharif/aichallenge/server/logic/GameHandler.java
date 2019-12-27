package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.common.network.data.ClientMessageInfo;
import ir.sharif.aichallenge.server.common.network.data.Message;
import ir.sharif.aichallenge.server.engine.core.GameLogic;

import java.util.List;
import java.util.Map;

public class GameHandler implements GameLogic {
    private static final int RESPONSE_TIMEOUT = 300;

    private Game game;

    @Override
    public int getClientsNum() {
        return 4;
    }

    @Override
    public long getClientResponseTimeout() {
        return RESPONSE_TIMEOUT;
    }

    @Override
    public long getTurnTimeout() {
        return 0;
    }

    @Override
    public void init() {
        game.init();
    }

    @Override
    public Message getUIInitialMessage() {
        return null;
    }

    @Override
    public Message[] getClientInitialMessages() {
        return new Message[0];
    }

    @Override
    public void simulateEvents(Map<String, List<ClientMessageInfo>> events) {
        if (game.getCurrentTurn().get() == 0) {
            game.pick(events);
            return;
        }
        game.turn(events);
    }

    @Override
    public void generateOutputs() {

    }

    @Override
    public Message getUIMessage() {
        return null;
    }

    @Override
    public Message getStatusMessage() {
        return null;
    }

    @Override
    public Message[] getClientMessages() {
        return new Message[0];
    }

    @Override
    public boolean isGameFinished() {
        return false;
    }

    @Override
    public void terminate() {

    }
}
