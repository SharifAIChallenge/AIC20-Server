package ir.sharif.aichallenge.server.logic.dto.client.turn;


import ir.sharif.aichallenge.server.logic.dto.client.ClientCell;
import lombok.*;

import java.util.Set;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnUnit {
    private int unitId;
    private int playerId;
    private int typeId;
    private int pathId;     // valid for player and his friend
    private ClientCell cell;
    private int hp;
    private int damageLevel;
    private int rangeLevel;
    private boolean isDuplicate;
    private boolean isHasted;
    private Set<Integer> affectedSpells;
    private int range;
    private int attack;
    private boolean wasPlayedThisTurn;
    private boolean wasRangeUpgraded;
    private boolean wasDamageUpgraded;
    private int target;
    private ClientCell targetCell;
}
