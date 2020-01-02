package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphicUnit {
    private int id;
    private int typeId;
    private int row;
    private int col;
    private int hp;
    private int damageLevel;    //todo;
    private int rangeLevel;
}
