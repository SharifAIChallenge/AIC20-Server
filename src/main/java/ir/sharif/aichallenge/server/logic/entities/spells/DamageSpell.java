package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;

public class DamageSpell extends AreaSpell {
    public static final int TYPE = 1;

    public DamageSpell(int id, Player player, Cell position) {
        super(id, BaseSpell.getInstance(TYPE), player, position);
    }

    @Override
    protected void applyEffectOn(Unit unit) {
        unit.decreaseHealth(getPower());
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
