package ir.sharif.aichallenge.server.logic.entities.units;

import ir.sharif.aichallenge.server.logic.entities.Entity;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.TargetType;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.MapUtils;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

import java.util.Comparator;
import java.util.stream.Stream;

@Getter
public abstract class Unit extends Entity {
    @Delegate
    private BaseUnit baseUnit;
    @Delegate()
    @Setter
    private PathCell position;
    private int speedIncrease;
    private int damageLevel = 0;
    private int rangeLevel = 0;
    private static int unitId = 1;

    private Unit targetUnit;

    public void upgradeDamage() {
        damageLevel++;
    }

    public void upgradeRange() {
        rangeLevel++;
    }

    private boolean hasAttacked;

    public Unit(BaseUnit baseUnit, Player player) {
        super(unitId, player);
        unitId++;
        this.baseUnit = baseUnit;
    }

    @Override
    public boolean isEnemy(Entity other) {
        return getId() != other.getId();
    }

    public abstract int getHealth();

    public abstract void increaseHealth(int heal);

    public abstract void decreaseHealth(int damage);

    public abstract void increaseActivePoisons();

    public abstract void decreaseActivePoisons();

    public abstract int getActivePoisons();

    public abstract boolean getIsCloned();

    public boolean isAlive() {
        return getHealth() > 0;
    }

    public int getDamage() {
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

    public Unit getTarget(Map map) {
        if (targetUnit == null || !targetUnit.isAlive() ||
                MapUtils.calcManhattanDistance(getCell(), targetUnit.getCell()) > getRange())
            resetTargetUnit(map);
        return targetUnit;
    }

    private void resetTargetUnit(Map map) {
        //First filtering target enemies in range
        Stream<Unit> units = map.getUnitsInManhattanRange(getCell(), getRange())
                .filter(this::isTarget);
        //Find nearest target
        targetUnit = units.findFirst()
                .flatMap(unit ->
                        //If present look in that distance
                        map.getUnitsWithManhattanDistance(getCell(), MapUtils.calcManhattanDistance(getCell(), unit.getCell()))
                                //To find best one with min health the max damage
                                .min(Comparator.comparingInt(Unit::getHealth)
                                        .thenComparing(Comparator.comparingInt(Unit::getDamage).reversed())))
                .orElse(null);
    }

    public boolean isTarget(Unit unit) {
        return isEnemy(unit) &&
                getTargetType() == TargetType.BOTH || unit.getMoveType().value == getTargetType().value;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }
}
