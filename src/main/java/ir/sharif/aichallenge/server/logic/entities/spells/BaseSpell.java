package ir.sharif.aichallenge.server.logic.entities.spells;

import lombok.Getter;

@Getter
public class BaseSpell {
    private int type;
    private int duration;
    private int range;
    private SpellTargetType targetType;
    private int power;
}
