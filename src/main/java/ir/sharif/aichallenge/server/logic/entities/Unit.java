package ir.sharif.aichallenge.server.logic.entities;

import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

@Getter
public class Unit {
    private int unitId;
    @Delegate
    private BaseUnit baseUnit;
    @Delegate()
    @Setter
    private PathCell position;
    private int health;
    private int damage;
    private int speed;
    private Player player;

    public Unit(int unitId, BaseUnit baseUnit, Player player) {
        this.unitId = unitId;
        this.baseUnit = baseUnit;
        this.player = player;
        this.health = baseUnit.getBaseHealth();
        this.damage = baseUnit.getBaseDamage();
        this.speed = baseUnit.getBaseSpeed();
    }

    boolean isEnemy(Unit other) {
        return true;
    }

    boolean canAttack(Map map) {
        return true;
    }

    Cell getNextCell() {
        return null;
    }

    void move() {
    }

}
