package ir.sharif.aichallenge.server.logic.entities;

import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Map;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

import java.util.Optional;

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
    private int range;
    private Player player;
    private Unit targetUnit;

    public Unit(int unitId, BaseUnit baseUnit, Player player) {
        this.unitId = unitId;
        this.baseUnit = baseUnit;
        this.player = player;
        this.health = baseUnit.getBaseHealth();
        this.damage = baseUnit.getBaseDamage();
        this.speed = baseUnit.getBaseSpeed();
        this.range = baseUnit.getRange();
    }

    boolean isEnemy(Unit other) {
        return true;
    }

    public boolean canAttack(Map map) {
        return map.getNearestTargetUnit(position.getCell().getRow(), position.getCell().getCol(),
                getRange(), baseUnit.getTargetType()).isPresent();
    }

    public Unit getTarget(Map map) {
        if (targetUnit == null || targetUnit.getHealth() <= 0 || getManhattanDistance(getCell(), targetUnit.getCell()) > getRange())
            resetTargetUnit(map);
        return targetUnit;
    }

    private int getManhattanDistance(Cell cell1, Cell cell2) {
        return Math.abs(cell1.getRow() - cell2.getRow()) + Math.abs(cell1.getCol() - cell2.getCol());
    }

    private void resetTargetUnit(Map map) {
        Optional<Unit> result = map.getNearestTargetUnit(position.getCell().getRow(), position.getCell().getCol(),
                getRange(), baseUnit.getTargetType());

        targetUnit = result.orElse(null);
    }

    Cell getNextCell() {
        return null;
    }

    void move() {
    }

    public void decreaseHealth(int damage) {
        health -= damage;
        health = Math.max(0, health);
    }
}
