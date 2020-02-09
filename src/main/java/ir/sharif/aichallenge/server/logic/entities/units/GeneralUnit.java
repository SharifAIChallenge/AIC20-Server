package ir.sharif.aichallenge.server.logic.entities.units;

import ir.sharif.aichallenge.server.logic.entities.Player;
import lombok.Getter;

@Getter
public class GeneralUnit extends Unit {
    private int health;
    private int damage;
    private boolean isDuplicate = false;
    private int activePoisons = 0;

    private boolean hasAttacked;

    public GeneralUnit(BaseUnit baseUnit, Player player,
                       int health, int damage) {
        super(baseUnit, player);
        this.health = health;
        this.damage = damage;
    }

    public void setDuplicate() {
        isDuplicate = true;
    }

    public GeneralUnit(BaseUnit baseUnit, Player player) {
        this(baseUnit, player,
                baseUnit.getBaseHealth(), -1);
    }

    @Override
    public int getSpeed() {
        return getBaseSpeed() + getSpeedIncrease();
    }

    @Override
    public int getDamage() {
        if (this.damage == -1)
            return super.getDamage();
        return this.damage;
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

    @Override
    public boolean isDuplicate() {
        return isDuplicate;
    }

}
