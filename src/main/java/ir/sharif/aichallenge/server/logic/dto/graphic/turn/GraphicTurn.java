package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GraphicTurn {
    private int turnNum;
    private List<PlayerTurnEvent> playerTurnEvents;
}
