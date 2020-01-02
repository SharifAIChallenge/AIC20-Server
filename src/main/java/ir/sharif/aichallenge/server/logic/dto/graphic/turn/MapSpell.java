package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicCell;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MapSpell {
    private int spellId;
    private GraphicCell center;
    private int range;
    private int typeId;
}
