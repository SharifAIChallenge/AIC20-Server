public class Unit {
    PathCell position;
    int level, health, damage, speed, unitId;
    Player player;

    boolean canAttack(Map map) {
        return true;
    }

    Cell getNextCell() {
        return null;
    }

    void move() {
    }

}
