package ir.sharif.aichallenge.server.logic.dto.init;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientSpell {
    private int typeId;
    private int turnEffect;
    private boolean isAreaSpell;
    private int range;          //invalid for unit spell
    private int power;          //invalid for unit spell
    private boolean isDamaging; //invalid for unit spell
}
