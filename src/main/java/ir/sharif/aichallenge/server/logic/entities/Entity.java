package ir.sharif.aichallenge.server.logic.entities;

import lombok.Getter;

@Getter
public class Entity {
    private int id;

    private Player player;

    public Entity(int id, Player player) {
        this.id = id;
        this.player = player;
    }

    public boolean isEnemy(Entity other) {
        return this.getPlayer().isEnemy(other.getPlayer());
    }

    public boolean isAlly(Entity other) {
        return isSelfEntity(other) || this.getPlayer().isAllyExclusive(other.getPlayer());
    }

    public boolean isSelfEntity(Entity other) {
        return this.getPlayer().getId() == other.getPlayer().getId();
    }

}
