package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;

public class HasteSpell extends AreaSpell {
    public static final int TYPE = 0;
    private static final int PRIORITY = 2;


    public HasteSpell(int id, Player player, Cell position) {
        //No need to get baseSpell from constructor
        super(id, BaseSpell.getInstance(TYPE), player, position);
    }

    @Override
    protected void applyEffectOn(Unit unit) {
        unit.setSpeedIncrease(getPower());
    }

    @Override
    public int getPriority() {
        return 2;
    }
}
