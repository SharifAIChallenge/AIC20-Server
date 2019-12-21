package ir.sharif.aichallenge.server.logic.entities;

import ir.sharif.aichallenge.server.logic.map.Map;

public class Spell {

    BaseSpell baseSpell;
    int id, remainingTurn;
    Player player;

    public void applyTo(Map map) {
    }

    public void decreaseTime() {
        remainingTurn --;
    }

    public boolean shouldRemove() {
        return remainingTurn == 0;
    }
}
