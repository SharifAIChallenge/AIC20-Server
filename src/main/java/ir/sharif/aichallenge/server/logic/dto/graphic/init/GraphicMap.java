package ir.sharif.aichallenge.server.logic.dto.graphic.init;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphicMap {
    private int row;
    private int col;
    private List<GraphicKing> kings;
    private List<GraphicPath> paths;
}
