package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;

public class HasteSpell extends StickySpell {

    public HasteSpell(int id, BaseSpell baseSpell, Player player, Cell position) {
        //No need to get baseSpell from constructor
        super(id, baseSpell, player, position);
    }

    @Override
    protected void applyEffectOn(Unit unit) {
        unit.setSpeedIncrease(getPower());
    }
}
