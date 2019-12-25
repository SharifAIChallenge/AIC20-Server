package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Map;

public abstract class StaticSpell extends Spell {

    public StaticSpell(int id, BaseSpell baseSpell, Player player, Cell position) {
        super(id, baseSpell, player, position);
    }

    @Override
    public void applyTo(Map map) {
        if (isDisposed())
            return;

        getTargetUnitsInRange(map).forEach(this::applyEffectOn);
    }
}
