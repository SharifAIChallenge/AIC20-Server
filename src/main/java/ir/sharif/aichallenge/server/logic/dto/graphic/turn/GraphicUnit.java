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

    public static GraphicUnit getGraphicUnit(Unit unit) {
        GraphicUnit graphicUnit = new GraphicUnit();
        graphicUnit.setId(unit.getId());
        graphicUnit.setTypeId(unit.getBaseUnit().getType());
        graphicUnit.setRow(unit.getCell().getRow());
        graphicUnit.setCol(unit.getCell().getCol());
        graphicUnit.setDamageLevel(unit.getDamageLevel());
        graphicUnit.setRangeLevel(unit.getRangeLevel());
        graphicUnit.setHp(unit.getHealth());
        return graphicUnit;
    }
}
