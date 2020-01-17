package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnCastSpell;
import ir.sharif.aichallenge.server.logic.entities.Entity;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.exceptions.LogicException;
import ir.sharif.aichallenge.server.logic.exceptions.SpellNotInMapException;
import ir.sharif.aichallenge.server.logic.map.Cell;
import lombok.Getter;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AreaSpell extends Spell {

    @Getter
    protected Collection<Unit> caughtUnits;

    public AreaSpell(int id, BaseSpell baseSpell, Player player, Cell position) {
        super(id, baseSpell, player, position);
    }

    @Override
    public void applyTo(Game game) {
        if (isDisposed())
            return;

        if (isFirstTurn())
            caughtUnits = getTargetUnitsInRange(game.getMap()).collect(Collectors.toList());

        caughtUnits.forEach(this::applyEffectOn);
    }

    @Override
    public TurnCastSpell getTurnCastSpell() {
        final TurnCastSpell turnCastSpell = super.getTurnCastSpell();
        turnCastSpell.setAffectedUnits(caughtUnits.stream().map(Entity::getId).collect(Collectors.toList()));
        return turnCastSpell;
    }

    @Override
    public void checkValid(Game game) throws LogicException {
        if(this.getPosition().getRow() < 0 ||
                this.getPosition().getCol() < 0 ||
                this.getPosition().getRow() >= game.getMap().getHeight() ||
                this.getPosition().getCol() >= game.getMap().getWidth())
            throw new SpellNotInMapException(this.getPlayer().getId(), this.getPosition());
    }
}
