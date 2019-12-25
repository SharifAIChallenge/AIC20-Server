package ir.sharif.aichallenge.server.logic.entities.units;

import ir.sharif.aichallenge.server.logic.entities.Entity;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

@Getter
public abstract class Unit extends Entity {
    @Delegate
    private BaseUnit baseUnit;
    @Delegate()
    @Setter
    private PathCell position;

    private int speedIncrease;

    private boolean hasAttacked;

    public Unit(int id, BaseUnit baseUnit, Player player) {
        super(id, player);
        this.baseUnit = baseUnit;
    }

    public abstract int getHealth();

    public abstract void increaseHealth(int heal);

    public abstract void decreaseHealth(int damage);

    public boolean isAlive() {
        return getHealth() > 0;
    }

    public abstract int getDamage();

    public abstract int getSpeed();

    public int getSpeedIncrease() {
        return this.speedIncrease;
    }

    public void setSpeedIncrease(int speedIncrease) {
        this.speedIncrease = speedIncrease;
    }

    public PathCell getNextMoveCell() {
        return position.nextCell(getSpeed());
    }

    public abstract Unit getTarget(Map map);

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }
}
