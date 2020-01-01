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
    private boolean isCloned = false;
    private int activePoisons = 0;

    private boolean hasAttacked;

    public GeneralUnit(BaseUnit baseUnit, Player player,
                       int health, int damage) {
        super(baseUnit, player);
        this.health = health;
        this.damage = damage;
    }

    public void setCloned() {
        isCloned = true;
    }

    public GeneralUnit(BaseUnit baseUnit, Player player) {
        this(baseUnit, player,
                baseUnit.getBaseHealth(), baseUnit.getBaseDamage());
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

    @Override
    public boolean isCloned() {
        return isCloned;
    }

}
