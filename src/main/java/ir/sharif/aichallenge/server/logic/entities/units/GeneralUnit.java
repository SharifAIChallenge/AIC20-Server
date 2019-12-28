package ir.sharif.aichallenge.server.logic.entities.units;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.MapUtils;
import lombok.Getter;

import java.util.Optional;

@Getter
public class GeneralUnit extends Unit {
    private int health;
    private int damage;
    private Unit targetUnit;
    private boolean hasAttacked;

    public GeneralUnit(BaseUnit baseUnit, Player player,
                       int health, int damage) {
        super(baseUnit, player);
        this.health = health;
        this.damage = damage;
    }

    public GeneralUnit(BaseUnit baseUnit, Player player) {
        this(baseUnit, player,
                baseUnit.getBaseHealth(), baseUnit.getBaseDamage());
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
    public int getSpeed() {
        return getBaseSpeed() + getSpeedIncrease();
    }

    @Override
    public void increaseHealth(int heal) {
        this.health += heal;
        this.health = Math.min(getBaseHealth(), this.health);
    }

    @Override
    public void decreaseHealth(int damage) {
        health -= damage;
        health = Math.max(0, health);
    }

}
