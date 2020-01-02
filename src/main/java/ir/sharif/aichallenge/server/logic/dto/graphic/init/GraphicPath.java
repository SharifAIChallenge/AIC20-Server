package ir.sharif.aichallenge.server.logic.dto.graphic.init;

import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphicPath {
    private int pathId;
    private List<GraphicCell> cells;
}
