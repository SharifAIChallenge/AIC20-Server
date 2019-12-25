package ir.sharif.aichallenge.server.logic.entities;

import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

@Getter
public abstract class Unit {
    private int id;
    @Delegate
    private BaseUnit baseUnit;
    private Player player;
    @Delegate()
    @Setter
    private PathCell position;

    private boolean hasAttacked;

    public Unit(int id, BaseUnit baseUnit, Player player) {
        this.id = id;
        this.baseUnit = baseUnit;
        this.player = player;
    }

    public abstract int getHealth();

    public abstract void decreaseHealth(int damage);

    public boolean isAlive() {
        return getHealth() > 0;
    }

    public abstract int getDamage();

    public abstract int getSpeed();

    public PathCell getNextMoveCell() {
        return position.nextCell(getSpeed());
    }

    public boolean isEnemy(Unit other) {
        return true;
    }

    public abstract Unit getTarget(Map map);

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }
}
