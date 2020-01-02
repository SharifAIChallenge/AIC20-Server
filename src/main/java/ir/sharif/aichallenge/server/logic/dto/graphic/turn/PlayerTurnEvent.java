package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.entities.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerTurnEvent {
    private int pId;
    private TurnEvent turnEvent;

    public static PlayerTurnEvent getGraphicPlayerTurnEvent(Player player, Game game) {
        PlayerTurnEvent playerTurnEvent = new PlayerTurnEvent();
        playerTurnEvent.setPId(player.getId());
        TurnEvent turnEvent = TurnEvent.getGraphicTurnEvent(player, game);
        playerTurnEvent.setTurnEvent(turnEvent);
        return playerTurnEvent;
    }
}
