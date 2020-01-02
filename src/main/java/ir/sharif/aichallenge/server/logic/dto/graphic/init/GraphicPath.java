package ir.sharif.aichallenge.server.logic.dto.graphic.init;

import ir.sharif.aichallenge.server.logic.dto.client.init.ClientPath;
import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphicPath {
    private int pathId;
    private List<GraphicCell> cells;

    public static GraphicPath makeGraphicPath(ClientPath clientPath) {
        GraphicPath graphicPath = new GraphicPath();
        graphicPath.setPathId(clientPath.getId());
        graphicPath.setCells(
                clientPath.getCells().stream().map(
                        GraphicCell::makeGraphicCell
                ).collect(Collectors.toList())
        );
        return graphicPath;
    }
}
