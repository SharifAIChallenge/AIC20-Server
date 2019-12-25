package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;

import java.util.stream.Collectors;

public class DuplicateSpell extends StickySpell {
    public DuplicateSpell(int id, BaseSpell baseSpell, Player player, Cell position) {
        super(id, baseSpell, player, position);
    }

    @Override
    public void applyTo(Game game) {
        if (isFirstTurn())
            cachedUnits = getTargetUnitsInRange(game.getMap())
                    .map(unit -> game.cloneUnit(unit, getPower(), getPower()))
                    .collect(Collectors.toSet());

        //Killing all cloned units after spell is disposed
        if (isDisposed())
            cachedUnits.forEach(unit -> unit.decreaseHealth(unit.getHealth()));
    }

    @Override
    protected void applyEffectOn(Unit unit) {
    }
}
