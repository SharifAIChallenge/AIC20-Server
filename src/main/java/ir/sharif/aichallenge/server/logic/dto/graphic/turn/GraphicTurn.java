package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphicTurn {
    private int turnNum;
    private List<PlayerTurnEvent> playerTurnEvents;
}
