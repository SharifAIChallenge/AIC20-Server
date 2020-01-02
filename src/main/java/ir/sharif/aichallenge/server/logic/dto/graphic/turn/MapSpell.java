package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapSpell {
    private int spellId;
    private GraphicCell center;
    private int range;
    private int typeId;
}
