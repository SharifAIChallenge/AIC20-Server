package ir.sharif.aichallenge.server.logic.dto.graphic.init;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphicInit {
    private GraphicConstants constants;
    private GraphicMap graphicMap;
}
