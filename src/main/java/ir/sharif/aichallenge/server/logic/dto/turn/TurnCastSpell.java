package ir.sharif.aichallenge.server.logic.dto.turn;

import ir.sharif.aichallenge.server.logic.dto.ClientCell;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnCastSpell {
    private int id;
    private int typeId;
    private int casterId;
    private ClientCell cell;
    private boolean wasCastedThisTurn;
    private int unitId;         // for unit spell
    private int pathId;         // for unit spell and caster == player,friend
    private List<Integer> affectedUnits;
}
