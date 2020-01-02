package ir.sharif.aichallenge.server.logic.dto.graphic;

import ir.sharif.aichallenge.server.logic.dto.graphic.init.GraphicInit;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.GraphicTurn;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GraphicMessage {
    private GraphicInit init;
    private List<GraphicTurn> turns;
}
