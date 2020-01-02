package ir.sharif.aichallenge.server.logic.dto.graphic.init;

import ir.sharif.aichallenge.server.logic.dto.client.init.ClientMap;
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
public class GraphicMap {
    private int row;
    private int col;
    private List<GraphicKing> kings;
    private List<GraphicPath> paths;

    public static GraphicMap makeGraphicMap(ClientMap map) {
        GraphicMap graphicMap = new GraphicMap();
        graphicMap.setRow(map.getRows());
        graphicMap.setCol(map.getCols());
        graphicMap.setKings(
                map.getKings().stream().map(
                        GraphicKing::makeGraphicKing
                ).collect(Collectors.toList())
        );
        graphicMap.setPaths(
                map.getPaths().stream().map(
                        GraphicPath::makeGraphicPath
                ).collect(Collectors.toList())
        );

        return graphicMap;
    }
}
