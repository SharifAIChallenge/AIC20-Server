package ir.sharif.aichallenge.server.logic.entities;

import lombok.Getter;

@Getter
public class Player {
    private int id;

    public void updateDeck() {
        //update deck.
    }

    public int getTeam() {
        return getId() % 2;
    }

    public boolean isEnemy(Player other) {
        return this.getTeam() != other.getTeam();
    }

    public boolean isAlly(Player other) {
        return this.getTeam() == other.getTeam();
    }

    public boolean isAllyExclusive(Player other) {
        return this.getId() != other.getId() && this.getTeam() == other.getTeam();
    }
}
