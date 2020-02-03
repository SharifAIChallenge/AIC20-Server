package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.dto.client.ClientCell;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnCastSpell;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.KingUnit;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.exceptions.LogicException;
import ir.sharif.aichallenge.server.logic.exceptions.TeleportKingException;
import ir.sharif.aichallenge.server.logic.exceptions.TeleportUnitIdException;
import ir.sharif.aichallenge.server.logic.exceptions.TooFarTeleportException;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    public void applyTo(Game game) throws LogicException {
        if (isDisposed())
            return;
        caughtUnit = game.getUnitById(targetUnitId);
        game.teleportUnit(caughtUnit, targetCell);  //Take care about direction of targetcell
    }

    @Override
    public void checkValid(Game game) throws LogicException {
        Unit unit = game.getUnitById(targetUnitId);

        if (unit == null)
            throw new TeleportUnitIdException(targetUnitId, getPlayer().getId());

        if (unit.getPlayer().getId() != getPlayer().getId())
            throw new TeleportUnitIdException(targetUnitId, getPlayer().getId());

        if (unit instanceof KingUnit)
            throw new TeleportKingException();

        int index = targetCell.getMoveIndex();
        List<Integer> list = new ArrayList<>(2);
        for (int i = 0; i < 4; i++) {
            int kingIndex = targetCell.getPath().getIndexForKing(i);
            if (kingIndex != 0 && kingIndex != targetCell.getPath().getLength() - 1)
                list.add(kingIndex);
        }

        if (index > (list.get(0) + list.get(1)) / 2)
            throw new TooFarTeleportException(getPlayer().getId(), targetCell);
    }

    @Override
    public Collection<Unit> getCaughtUnits() {
        return Collections.singletonList(caughtUnit);
    }

    @Override
    public TurnCastSpell getTurnCastSpell() {
        final TurnCastSpell turnCastSpell = super.getTurnCastSpell();
        turnCastSpell.setCell(new ClientCell(targetCell.getCell()));
        turnCastSpell.setUnitId(getTargetUnitId());
        turnCastSpell.setPathId(getTargetCell().getPath().getId());
        turnCastSpell.setAffectedUnits(Collections.singletonList(getTargetUnitId()));
        return turnCastSpell;
    }

}
