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

    private int damageLevel = 0, rangeLevel = 0;

    public void upgradeDamage(){
        damageLevel++;
    }

    public void upgradeRange(){
        rangeLevel++;
    }

    private boolean hasAttacked;

    public Unit(int id, BaseUnit baseUnit, Player player) {
        super(id, player);
        this.baseUnit = baseUnit;
    }

    @Override
    public boolean isEnemy(Entity other) {
        return getId() != other.getId();
    }

    public abstract int getHealth();

    public abstract void increaseHealth(int heal);

    public abstract void decreaseHealth(int damage);

    public boolean isAlive() {
        return getHealth() > 0;
    }

    public int getDamage(){
        return this.baseUnit.getBaseDamage() + damageLevel * this.baseUnit.getDeltaDamage();
    }

    public int getRange() {
        return this.baseUnit.getBaseDamageRange() + rangeLevel + this.baseUnit.getDeltaDamageRange();
    }

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
