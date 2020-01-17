package ir.sharif.aichallenge.server.logic;

import com.google.gson.Gson;
import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicMessage;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.GraphicTurn;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.PlayerTurnEvent;
import ir.sharif.aichallenge.server.logic.entities.Player;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;

public class GraphicHandler {

    private boolean firstSave = true;

    @Getter
    private RandomAccessFile file;

    public GraphicTurn getGraphicTurn(Game game) {
        GraphicTurn graphicTurn = new GraphicTurn();

        graphicTurn.setTurnNum(game.getCurrentTurn().get());
        ArrayList<PlayerTurnEvent> playerTurnEvents = new ArrayList<>();

        for (int pId = 0; pId < 4; pId++) {
            Player player = game.getPlayers()[pId];
            PlayerTurnEvent playerTurnEvent = PlayerTurnEvent.getGraphicPlayerTurnEvent(player, game);
            playerTurnEvents.add(playerTurnEvent);
        }

        graphicTurn.setPlayerTurnEvents(playerTurnEvents);
        graphicTurn.setTurnAttacks(game.getCurrentAttacks());

        return graphicTurn;
    }

    private void initSaver() {
        try {

            File deleteLastLog = new File("graphic.json");
            deleteLastLog.delete();
            file = new RandomAccessFile("graphic.json", "rwd");

        } catch (Exception ex) {
            System.out.println("Cannot Open graphic.json");
        }
    }

    public void saveGraphicLog(GraphicMessage graphicMessage, GraphicTurn graphicTurn) {
        if (firstSave) {

            initSaver();

            String graphicLog = new Gson().toJson(graphicMessage);
            try {
                file.write(graphicLog.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            firstSave = false;
        }
        else {

            String turnLog = "," + new Gson().toJson(graphicTurn);
            try {
                long pos = file.length();
                pos -= 2;
                file.seek(pos);
                file.write(turnLog.getBytes());
                pos = file.length();
                file.seek(pos);
                file.write("]}".getBytes());
            } catch (Exception ex) { }
        }
    }
}
