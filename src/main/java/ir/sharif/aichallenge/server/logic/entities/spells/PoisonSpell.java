package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;

public class PoisonSpell extends StickySpell {
    public static final int TYPE = 5;

    public PoisonSpell(int id, Player player, Cell position) {
        super(id, BaseSpell.getInstance(TYPE), player, position);
    }

    @Override
    protected void applyEffectOn(Unit unit) {
        unit.decreaseHealth(getPower());
    }
}
