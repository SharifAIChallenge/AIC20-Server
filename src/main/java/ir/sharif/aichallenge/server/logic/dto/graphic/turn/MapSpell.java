package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapSpell {
    private int spellId;
//    private GraphicCell center;
//    private int range;
    private List<Integer> unitIds;
    private int typeId;
}
