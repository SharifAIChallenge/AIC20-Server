package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.dto.ClientCell;
import ir.sharif.aichallenge.server.logic.dto.turn.TurnCastSpell;
import ir.sharif.aichallenge.server.logic.entities.Entity;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AreaSpell extends Spell {

    @Getter
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

    @Override
    public TurnCastSpell getTurnCastSpell() {
        final TurnCastSpell turnCastSpell = super.getTurnCastSpell();
        turnCastSpell.setAffectedUnits(caughtUnits.stream().map(Entity::getId).collect(Collectors.toList()));
        return turnCastSpell;
    }
}
