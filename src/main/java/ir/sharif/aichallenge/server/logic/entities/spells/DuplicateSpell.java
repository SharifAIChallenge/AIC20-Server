package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;

import java.util.stream.Collectors;

public class DuplicateSpell extends AreaSpell {
    public DuplicateSpell(int id, Player player, Cell position) {
        super(id, BaseSpell.getInstance(SpellType.DUPLICATE), player, position);
    }

    @Override
    public void applyTo(Game game) {
        if (isFirstTurn())
            caughtUnits = getTargetUnitsInRange(game.getMap())      //todo what happens to caught units??
                    .map(unit -> game.cloneUnit(unit, getPower(), getPower()))  //todo filter cloned units
                    .collect(Collectors.toSet());

        //Killing all cloned units after spell is disposed
        if (isDisposed())   //todo we check to kill cloned units twice
            caughtUnits.forEach(unit -> unit.decreaseHealth(Integer.MAX_VALUE));
    }

    @Override
    public boolean isTarget(Unit unit) {
        return super.isTarget(unit) && !unit.isCloned();
    }
}
