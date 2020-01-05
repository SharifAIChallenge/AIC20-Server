package ir.sharif.aichallenge.server.logic;

import com.google.gson.Gson;
import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicMessage;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.GraphicTurn;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.PlayerTurnEvent;
import ir.sharif.aichallenge.server.logic.entities.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class GraphicHandler {

    public GraphicTurn getGraphicTurn(Game game) {
        GraphicTurn graphicTurn = new GraphicTurn();

        graphicTurn.setTurnNum(game.getCurrentTurn().get());
        ArrayList<PlayerTurnEvent> playerTurnEvents = new ArrayList<>();

        for (int pId=0; pId<4; pId++) {
            Player player = game.getPlayers()[pId];
            PlayerTurnEvent playerTurnEvent = PlayerTurnEvent.getGraphicPlayerTurnEvent(player, game);
            playerTurnEvents.add(playerTurnEvent);
        }

        graphicTurn.setPlayerTurnEvents(playerTurnEvents);
        graphicTurn.setTurnAttacks(game.getCurrentAttacks());

        return graphicTurn;
    }

    public void saveGraphicLog(GraphicMessage graphicMessage) {
        String graphicLog = new Gson().toJson(graphicMessage);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("graphic.log"));
            bw.write(graphicLog);
            bw.close();
        } catch(Exception ex){}
    }
}
