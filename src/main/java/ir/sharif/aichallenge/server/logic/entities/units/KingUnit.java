package ir.sharif.aichallenge.server.logic.entities.units;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.map.Map;

public class KingUnit extends Unit {

    private HealthComponent health;

    public KingUnit(BaseUnit baseUnit, Player player, HealthComponent health) {
        super(baseUnit, player);
        this.health = health;
    }

    @Override
    public int getHealth() {
        return health.getHealth();
    }

    @Override
    public void setCloned() { }

    @Override
    public void increaseHealth(int heal) { }

    @Override
    public void decreaseHealth(int amount) {
        health.decrease(amount);
    }

    @Override
    public boolean isCloned() {
        return false;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public Unit getTarget(Map map) {
        return null;
    }


    public static class HealthComponent {
        private int health;

        public int getHealth() {
            return this.health;
        }

        public void decrease(int amount) {
            this.health -= amount;
        }

        public void setHealth(int health){ this.health = health; }
    }
}
