package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;

public class HealSpell extends StaticSpell {
    public HealSpell(int id, BaseSpell baseSpell, Player player, Cell position) {
        super(id, baseSpell, player, position);
    }

    @Override
    protected void applyEffectOn(Unit unit) {
        unit.increaseHealth(getPower());
    }
}
