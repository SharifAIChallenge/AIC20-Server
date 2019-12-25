package ir.sharif.aichallenge.server.logic.entities;

import lombok.Getter;

@Getter
public class Player {
    private int id;

    public void updateDeck() {
        //update deck.
    }

    public boolean isEnemy(Player other) {
        return false;
    }

    public boolean isAllyExclusive(Player other) {
        return false;
    }
}
