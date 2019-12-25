package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Map;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class StickySpell extends Spell {

    private Set<Unit> cachedUnits;

    public StickySpell(int id, BaseSpell baseSpell, Player player, Cell position) {
        super(id, baseSpell, player, position);
    }

    @Override
    public void applyTo(Map map) {
        if (isDisposed())
            return;

        if (isFirstTurn())
            cachedUnits = getTargetUnitsInRange(map).collect(Collectors.toSet());

        cachedUnits.forEach(this::applyEffectOn);
    }

}
