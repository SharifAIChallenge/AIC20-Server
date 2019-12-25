package ir.sharif.aichallenge.server.logic.dto.turn;

import ir.sharif.aichallenge.server.logic.dto.ClientCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnCastSpell {
    private int typeId;
    private int casterId;
    private ClientCell cell;
    private int unitId;         // for unit spell
    private int pathId;         // for unit spell and caster == player,friend
    private List<Integer> affectedUnits;
}
