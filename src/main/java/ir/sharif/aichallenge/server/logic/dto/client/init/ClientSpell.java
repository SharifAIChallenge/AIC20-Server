package ir.sharif.aichallenge.server.logic.dto.client.init;

import ir.sharif.aichallenge.server.logic.entities.spells.SpellTargetType;
import ir.sharif.aichallenge.server.logic.entities.spells.SpellType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientSpell {
    private SpellType type;
    private int typeId;
    private int priority;
    private int duration;
    private int range;
    private SpellTargetType target;
    private int power;
}
