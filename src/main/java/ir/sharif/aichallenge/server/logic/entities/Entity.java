package ir.sharif.aichallenge.server.logic.entities;

import lombok.Getter;

@Getter
public abstract class Entity {
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
        return this.getPlayer().isAlly(other.getPlayer());
    }

    public boolean isSelfEntity(Entity other) {
        return this.getPlayer().getId() == other.getPlayer().getId();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Entity)) return false;
        return ((Entity) obj).getId() == getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
