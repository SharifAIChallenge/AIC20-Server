package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.common.network.Json;
import ir.sharif.aichallenge.server.common.network.data.ClientMessageInfo;
import ir.sharif.aichallenge.server.common.network.data.Message;
import ir.sharif.aichallenge.server.common.network.data.MessageTypes;
import ir.sharif.aichallenge.server.common.network.data.PickInfo;
import ir.sharif.aichallenge.server.engine.config.FileParam;
import ir.sharif.aichallenge.server.engine.config.StringParam;
import ir.sharif.aichallenge.server.engine.core.GameLogic;
import ir.sharif.aichallenge.server.logic.dto.client.end.ClientEndMessage;
import ir.sharif.aichallenge.server.logic.dto.client.init.ClientBaseKing;
import ir.sharif.aichallenge.server.logic.dto.client.init.ClientMap;
import ir.sharif.aichallenge.server.logic.dto.client.init.InitialMessage;
import ir.sharif.aichallenge.server.logic.dto.client.turn.ClientTurnMessage;
import ir.sharif.aichallenge.server.logic.dto.serverlog.ServerLogHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GameHandler implements GameLogic {
    private static final FileParam PARAM_MAP = new FileParam("Map", null, ".*\\.map");
    private static final StringParam[] CLIENT_NAMES = new StringParam[4];

    private Game game = new Game();
    private InitialMessage initialMessage;
    private final int extraTime;

    public GameHandler(AtomicInteger currentTurn, int extraTime) {
        game.setCurrentTurn(currentTurn);
        this.extraTime = extraTime;
        for (int i = 0; i < 4; i++) {
            CLIENT_NAMES[i] = new StringParam("TeamName" + i, "Team" + i);
        }
    }

    @Override
    public int getClientsNum() {
        return 4;
    }

    @Override
    public boolean[] getActiveClients() {
        boolean[] retVal = new boolean[4];
        for (int i = 0; i < 4; i++) {
            retVal[i] = game.isPlayerAlive(i);
        }
        return retVal;
    }

    @Override
    public long getClientResponseTimeout() {
        if (game.getCurrentTurn().get() == 0) {
            return initialMessage.getGameConstants().getPickTimeout() + extraTime;
        }
        return initialMessage.getGameConstants().getTurnTimeout() + extraTime;
    }

    @Override
    public long getTurnTimeout() {
        return 0;
    }

    @Override
    public void init() {
        String initStr = readMapFile(PARAM_MAP);
        initialMessage = null;
        try {
            initialMessage = Json.GSON.fromJson(initStr, InitialMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Invalid map file!");
            System.exit(0);
        }

        String[] clientNames = new String[4];
        for (int i = 0; i < 4; i++) {
            clientNames[i] = CLIENT_NAMES[i].getValue();
        }

        game.init(initialMessage, clientNames);
    }

    private String readMapFile(FileParam paramMap) {
        StringBuilder result = new StringBuilder();
        File mapFile = paramMap.getValue();
        if (mapFile == null || !mapFile.exists()) {
            System.err.println("Invalid map file!");
            System.exit(0);
        }
        try (Scanner in = new Scanner(mapFile)) {
            while (in.hasNext()) {
                result.append(in.nextLine());
                result.append("\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Map file not found!");
            System.exit(0);
        }

        return result.toString();
    }

    @Override
    public Message getUIInitialMessage() {
        return null;
    }

    @Override
    public Message[] getClientInitialMessages() {
        Message[] initMessages = new Message[4];

        ClientMap map = initialMessage.getMap();
        List<ClientBaseKing> kings = map.getKings();
        ClientBaseKing[] sortedKings = new ClientBaseKing[4];
        for (ClientBaseKing king : kings) {
            sortedKings[king.getPlayerId()] = king;
        }

        map.setKings(Arrays.asList(sortedKings[0], sortedKings[2], sortedKings[1], sortedKings[3]));
        initMessages[0] = new Message(MessageTypes.INIT, Json.GSON.toJsonTree(initialMessage).getAsJsonObject());

        map.setKings(Arrays.asList(sortedKings[2], sortedKings[0], sortedKings[1], sortedKings[3]));
        initMessages[2] = new Message(MessageTypes.INIT, Json.GSON.toJsonTree(initialMessage).getAsJsonObject());

        map.setKings(Arrays.asList(sortedKings[1], sortedKings[3], sortedKings[0], sortedKings[2]));
        initMessages[1] = new Message(MessageTypes.INIT, Json.GSON.toJsonTree(initialMessage).getAsJsonObject());

        map.setKings(Arrays.asList(sortedKings[3], sortedKings[1], sortedKings[0], sortedKings[2]));
        initMessages[3] = new Message(MessageTypes.INIT, Json.GSON.toJsonTree(initialMessage).getAsJsonObject());

        return initMessages;
    }

    @Override
    public void simulateEvents(Map<String, List<ClientMessageInfo>> messages) {
        if (game.getCurrentTurn().get() == 0) {
            List<ClientMessageInfo> clientMessageInfos = messages.get(MessageTypes.PICK);
            if (clientMessageInfos == null) {
                game.pick(new ArrayList<>());
                return;
            }
            List<PickInfo> pickInfos = clientMessageInfos.stream().map(
                    clientMessageInfo -> (PickInfo) clientMessageInfo).collect(Collectors.toList());
            game.pick(pickInfos);
            return;
        }
        game.turn(messages);
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

    public ClientTurnMessage[] getClientRawMessages() {
        return game.getClientTurnMessages();
    }

    @Override
    public Message[] getClientMessages() {
        if (game.getCurrentTurn().get() == 0) {
            return getClientInitialMessages();
        }

        Message[] messages = new Message[4];
        // turn
        ClientTurnMessage[] clientTurnMessages = game.getClientTurnMessages();
        for (int i = 0; i < 4; i++) {
            messages[i] = new Message(MessageTypes.TURN,
                    Json.GSON.toJsonTree(clientTurnMessages[i], ClientTurnMessage.class).getAsJsonObject());
        }
        return messages;
    }

    @Override
    public Message[] getClientEndMessages() {
        Message[] messages = new Message[4];
        ClientEndMessage[] clientEndMessages = game.getClientEndMessages();
        for (int i = 0; i < 4; i++) {
            messages[i] = new Message(MessageTypes.SHUTDOWN,
                    Json.GSON.toJsonTree(clientEndMessages[i], ClientEndMessage.class).getAsJsonObject());
        }
        return messages;
    }

    @Override
    public boolean isGameFinished() {
        return game.isGameFinished();
    }

    @Override
    public void terminate() {
        try {
            game.getGraphicHandler().saveLastLog(game.getGraphicMessage());
            game.getGraphicHandler().getFile().close();
            ServerLogHandler.saveServerLog(game.getServerViewLog());
        } catch (Exception ex) {
        }
    }

    public int getCurrentTurn() {
        return game.getCurrentTurn().get();
    }
}
