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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Getter
public abstract class Unit extends Entity {
    @Delegate
    private BaseUnit baseUnit;
    @Delegate
    @Setter
    private PathCell position;
    private int speedIncrease;
    private int damageLevel = 0;
    private int rangeLevel = 0;
    private static int unitId = 1;

    private Unit targetUnit;
    private boolean hasAttacked;

    private Set<Integer> affectedSpells;

    public void upgradeDamage() {
        damageLevel++;
    }

    public void upgradeRange() {
        rangeLevel++;
    }

    public Unit(BaseUnit baseUnit, Player player) {
        super(unitId, player);
        unitId++;
        this.baseUnit = baseUnit;
    }

    @Override
    public boolean isEnemy(Entity other) {
        return this.getPlayer().isEnemy(other.getPlayer());
    }

    public abstract int getHealth();

    public abstract void increaseHealth(int heal);

    public abstract void decreaseHealth(int damage);

    public abstract boolean isDuplicate();

    public boolean isAlive() {
        return getHealth() > 0;
    }

    public int getDamage() {
        return this.baseUnit.getBaseDamage() + damageLevel * this.baseUnit.getDeltaDamage();
    }

    public int getRange() {
        return this.baseUnit.getBaseDamageRange() + rangeLevel * this.baseUnit.getDeltaDamageRange();
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

    public void findTarget(Map map) {
        if (targetUnit == null || !targetUnit.isAlive() ||
                MapUtils.calcManhattanDistance(getCell(), targetUnit.getCell()) > getRange())
            resetTargetUnit(map);
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
                                .filter(this::isTarget)
                                //To find best one with min health the max damage
                                .min(Comparator.comparingInt(Unit::getHealth)
                                        .thenComparing(Comparator.comparingInt(Unit::getDamage).reversed())))
                .orElse(null);
    }

    public boolean isTarget(Unit unit) {
        return isEnemy(unit) && (unit.getMoveType().value & getTargetType().value) != 0;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public void addActiveSpell(int spellId) {
        if (affectedSpells == null)
            affectedSpells = new HashSet<>();
        affectedSpells.add(spellId);
    }

    public void removeActiveSpell(int spellId) {
        if (affectedSpells == null)
            return;
        affectedSpells.remove(spellId);
    }

    public Set<Integer> getAffectedSpells() {
        if (affectedSpells == null)
            return Collections.emptySet();
        return Collections.unmodifiableSet(this.affectedSpells);
    }
}