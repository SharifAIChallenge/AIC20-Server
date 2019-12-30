package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class AreaSpell extends Spell {

    protected Set<Unit> caughtUnits;

    public AreaSpell(int id, BaseSpell baseSpell, Player player, Cell position) {
        super(id, baseSpell, player, position);
    }

    @Override
    public void applyTo(Game game) {
        if (isDisposed())
            return;

        if (isFirstTurn())
            caughtUnits = getTargetUnitsInRange(game.getMap()).collect(Collectors.toSet());

        caughtUnits.forEach(this::applyEffectOn);
    }

}
