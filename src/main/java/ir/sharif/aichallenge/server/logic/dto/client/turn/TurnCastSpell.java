package ir.sharif.aichallenge.server.logic.dto.client.turn;

import ir.sharif.aichallenge.server.logic.dto.client.ClientCell;
import ir.sharif.aichallenge.server.logic.entities.spells.SpellType;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnCastSpell {
    private int typeId;
    private int id;
    private int casterId;
    private ClientCell cell;
    private int unitId;         // for unit spell
    private int pathId;         // for unit spell and caster == player,friend
    private int remainingTurns;
    private boolean wasCastThisTurn;
    private List<Integer> affectedUnits;

    @Override
    public String toString() {
        return "Caster : " + casterId +
                " Casted Spell With Type : " + typeId +
                " With Center : " + "(" + cell.getRow() + "," + cell.getCol() + ")";
    }

}
