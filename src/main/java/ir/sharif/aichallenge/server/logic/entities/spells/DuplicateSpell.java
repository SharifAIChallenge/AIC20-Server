package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;

import java.util.stream.Collectors;

public class DuplicateSpell extends AreaSpell {
    public static final int TYPE = 4;

    public DuplicateSpell(int id, Player player, Cell position) {
        super(id, BaseSpell.getInstance(TYPE), player, position);
    }

    @Override
    public void applyTo(Game game) {
        if (isFirstTurn())
            caughtUnits = getTargetUnitsInRange(game.getMap())      //todo what happens to cached units??
                    .map(unit -> game.cloneUnit(unit, getPower(), getPower()))
                    .collect(Collectors.toSet());

        //Killing all cloned units after spell is disposed
        if (isDisposed())   //todo we check to kill cloned units twice
            caughtUnits.forEach(unit -> unit.decreaseHealth(unit.getHealth()));
    }

    @Override
    protected void applyEffectOn(Unit unit) {
    }

    @Override
    public int getPriority() {
        return 3;
    }
}
