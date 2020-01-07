package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import ir.sharif.aichallenge.server.logic.entities.units.KingUnit;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnAttack {
    private int attackerId;
    private int defenderId;

    public static TurnAttack getTurnAttack(Unit unit, Unit targetUnit) {
        int idSource = getIdForAttack(unit);
        int idTarget = getIdForAttack(targetUnit);
        return new TurnAttack(idSource, idTarget);

    }

    private static int getIdForAttack(Unit unit) {
        int id = unit.getId();
        if(unit instanceof KingUnit)
            id = unit.getPlayer().getId();
        return id;
    }
}
