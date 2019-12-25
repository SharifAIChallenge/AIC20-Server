package ir.sharif.aichallenge.server.logic.dto.init;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientBaseUnit {
    private int typeId;
    private int maxHP;
    private int baseAttack;
    private int baseRange;
    private String target;      //can be enum, values: GROUND, AIR, BOTH
    private boolean isFlying;
    private boolean isMultiple;
}
