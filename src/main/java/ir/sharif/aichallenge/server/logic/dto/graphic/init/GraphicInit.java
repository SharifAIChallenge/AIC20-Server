package ir.sharif.aichallenge.server.logic.dto.graphic.init;

import ir.sharif.aichallenge.server.logic.dto.client.init.InitialMessage;
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
public class GraphicInit {
    private int maxAP;
    private GraphicMap graphicMap;
    private List<GraphicBaseUnit> baseUnits;

    public static GraphicInit makeGraphicInit(InitialMessage initialMessage) {
        GraphicInit graphicInit = new GraphicInit();
        graphicInit.setMaxAP(initialMessage.getGameConstants().getMaxAP());
        graphicInit.setGraphicMap(GraphicMap.makeGraphicMap(initialMessage.getMap()));

        graphicInit.setBaseUnits(
                initialMessage.getBaseUnits().stream().map(
                        GraphicBaseUnit::makeGraphicBaseUnit
                ).collect(Collectors.toList()));

        return graphicInit;
    }
}
