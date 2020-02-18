package ir.sharif.aichallenge.server.logic;

import com.google.gson.Gson;
import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicMessage;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.GraphicTurn;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.PlayerTurnEvent;
import ir.sharif.aichallenge.server.logic.entities.Player;
import lombok.Getter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd----HH-mm-ss");
        Date date = new Date();

        try {
            File dirMaker = new File("Log");
            dirMaker.mkdir();

            file = new RandomAccessFile("Log/graphic--" + formatter.format(date) + ".json", "rwd");
            file.setLength(0);

        } catch (Exception ex) {
            System.out.println("Cannot Open graphic.json");
            System.out.println(ex.getMessage());
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

    public void saveLastLog(GraphicMessage graphicMessage) {
        String graphicLog = new Gson().toJson(graphicMessage);
        try {
            file.setLength(0);
            file.write(graphicLog.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
