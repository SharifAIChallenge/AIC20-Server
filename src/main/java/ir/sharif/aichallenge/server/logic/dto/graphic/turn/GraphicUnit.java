package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
