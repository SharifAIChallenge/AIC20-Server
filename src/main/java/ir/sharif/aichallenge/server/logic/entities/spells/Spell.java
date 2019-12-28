package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.entities.Disposable;
import ir.sharif.aichallenge.server.logic.entities.Entity;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.units.KingUnit;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Map;
import lombok.Getter;
import lombok.experimental.Delegate;

import java.util.stream.Stream;

@Getter
public abstract class Spell extends Entity implements Disposable {

    @Delegate
    private BaseSpell baseSpell;
    private int remainingTurns;

    private Cell position;

    public Spell(int id, BaseSpell baseSpell, Player player, Cell position) {
        super(id, player);
        this.baseSpell = baseSpell;
        this.position = position;
    }

    public abstract void applyTo(Game game);

    protected abstract void applyEffectOn(Unit unit);

    public boolean isTarget(Unit unit) {
        if (unit instanceof KingUnit) return false;
        switch (this.getTargetType()) {
            case SELF:
                return this.isSelfEntity(unit);
            case ALLIED:
                return this.isAlly(unit);
            case ENEMY:
                return this.isEnemy(unit);
            default:
                return false;
        }
    }

    protected Stream<Unit> getTargetUnitsInRange(Map map) {

        return map.getUnitsInArea(getPosition().getRow(), getPosition().getCol(), getRange())
                .filter(this::isTarget);
    }

    @Override
    public int getRemainingTurns() {
        return remainingTurns;
    }

    public void decreaseRemainingTurns() {
        this.remainingTurns--;
    }

    protected boolean isFirstTurn() {
        return getRemainingTurns() == getDuration();
    }

    public boolean shouldRemove() {
        return isDisposed();
    }
}
