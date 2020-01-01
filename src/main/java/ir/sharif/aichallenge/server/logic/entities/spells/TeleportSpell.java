package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.dto.turn.TurnCastSpell;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;

@Getter
public class TeleportSpell extends Spell {
    public static final int TYPE = 3;

    private int targetUnitId;
    private Unit caughtUnit;
    private PathCell targetCell;

    public TeleportSpell(int id, Player player, Cell position, int targetUnitId, PathCell targetCell) {
        super(id, BaseSpell.getInstance(SpellType.TELE), player, position);
        this.targetUnitId = targetUnitId;
        this.targetCell = targetCell;
    }

    @Override
    public void applyTo(Game game) {
        if (isDisposed())
            return;
        caughtUnit = game.getUnitById(targetUnitId);
        game.teleportUnit(caughtUnit, targetCell);  //Take care about direction of targetcell
    }

    @Override
    public Set<Unit> getCaughtUnits() {
        return Collections.singleton(caughtUnit);
    }

    @Override
    public TurnCastSpell getTurnCastSpell() {
        final TurnCastSpell turnCastSpell = super.getTurnCastSpell();
        turnCastSpell.setUnitId(getTargetUnitId());
        turnCastSpell.setPathId(getTargetCell().getPath().getId());
        turnCastSpell.setAffectedUnits(Collections.singletonList(getTargetUnitId()));
        return turnCastSpell;
    }
}
