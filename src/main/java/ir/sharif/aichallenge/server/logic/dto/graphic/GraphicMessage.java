package ir.sharif.aichallenge.server.logic.dto.graphic;

import ir.sharif.aichallenge.server.logic.dto.client.end.PlayerScore;
import ir.sharif.aichallenge.server.logic.dto.graphic.init.GraphicInit;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.GraphicTurn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphicMessage {
    private GraphicInit init;
    private List<PlayerScore> end;
    private List<GraphicTurn> turns = new ArrayList<>();
    }
