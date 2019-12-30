package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.PathCell;

import java.util.function.BiConsumer;

public class TeleportSpell extends Spell {
    public static final int TYPE = 3;


    private int targetUnitId;
    private PathCell targetCell;

    public TeleportSpell(int id, Player player, Cell position, int targetUnitId, PathCell targetCell) {
        super(id, BaseSpell.getInstance(TYPE), player, position);
        this.targetUnitId = targetUnitId;
        this.targetCell = targetCell;
    }

    @Override
    public void applyTo(Game game) {
        if (isDisposed())
            return;
        game.teleportUnit(game.getUnitById(targetUnitId), targetCell);  //Take care about direction of targetcell
    }

    @Override
    protected void applyEffectOn(Unit unit) {
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
