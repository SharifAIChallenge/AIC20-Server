package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;

import java.util.stream.Collectors;

public class DuplicateSpell extends AreaSpell {
    public DuplicateSpell(int id, BaseSpell baseSpell, Player player, Cell position) {
        super(id, baseSpell, player, position);
    }

    @Override
    public void applyTo(Game game) {
        if (isFirstTurn()) {
            caughtUnits = getTargetUnitsInRange(game.getMap())
                    .filter(unit -> !unit.isDuplicate())
                    .map(unit -> game.cloneUnit(unit, getPower(), getPower()))
                    .collect(Collectors.toList());
            caughtUnits.forEach(unit -> game.getMap().putUnit(unit));
        }
    }

    @Override
    public void decreaseRemainingTurns() {
        super.decreaseRemainingTurns();
        //Killing all cloned units after spell is disposed
        if (isDisposed())
            caughtUnits.forEach(unit -> unit.decreaseHealth(Integer.MAX_VALUE));
    }

    @Override
    public boolean isTarget(Unit unit) {
        return super.isTarget(unit) && !unit.isDuplicate();
    }
}
