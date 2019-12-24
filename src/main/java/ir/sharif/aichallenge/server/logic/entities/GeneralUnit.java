package ir.sharif.aichallenge.server.logic.entities;

import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.MapUtils;
import lombok.Getter;

import java.util.Optional;

@Getter
public class GeneralUnit extends Unit {
    private int health;
    private int damage;
    private int speed;
    private Unit targetUnit;
    private boolean hasAttacked;

    public GeneralUnit(int unitId, BaseUnit baseUnit, Player player,
                       int health, int damage, int speed) {
        super(unitId, baseUnit, player);
        this.health = health;
        this.damage = damage;
        this.speed = speed;
    }

    public GeneralUnit(int unitId, BaseUnit baseUnit, Player player) {
        this(unitId, baseUnit, player,
                baseUnit.getBaseHealth(), baseUnit.getBaseDamage(), baseUnit.getBaseSpeed());
    }


    public Unit getTarget(Map map) {
        if (targetUnit == null || targetUnit.getHealth() <= 0 || MapUtils.calcManhattanDistance(getCell(), targetUnit.getCell()) > getRange())
            resetTargetUnit(map);
        return targetUnit;
    }

    private void resetTargetUnit(Map map) {
        Optional<Unit> result = map.getNearestTargetUnit(this);

        targetUnit = result.orElse(null);
    }

    void move() {
    }

    @Override
    public void decreaseHealth(int damage) {
        health -= damage;
        health = Math.max(0, health);
    }

}
