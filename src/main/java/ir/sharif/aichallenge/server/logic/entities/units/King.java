package ir.sharif.aichallenge.server.logic.entities.units;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.map.Map;

public class King extends Unit {

    private HealthComponent health;

    public King(int unitId, BaseUnit baseUnit, Player player, HealthComponent health) {
        super(unitId, baseUnit, player);
        this.health = health;
    }

    @Override
    public int getHealth() {
        return health.getHealth();
    }

    @Override
    public void increaseHealth(int heal) {
    }

    @Override
    public void decreaseHealth(int amount) {
        health.decrease(amount);
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


    private static class HealthComponent {
        private int health;

        public int getHealth() {
            return this.health;
        }

        public void decrease(int amount) {
            this.health -= amount;
        }
    }
}
