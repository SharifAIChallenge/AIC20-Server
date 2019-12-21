package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.Spell;
import ir.sharif.aichallenge.server.logic.entities.Unit;
import ir.sharif.aichallenge.server.logic.map.Map;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Game {

    Map map;
    List<Spell>  spells = new ArrayList<>();
    List<Pair<Unit, Integer> > unitsToPut;
    Player[] players;

    public void init() {
        //make initial map and paths and players.
    }

    public void turn() {
        readRequestsFromClient();

        applySpells();
        applyPutUnits();

        evaluateSpells();

        moveAndAttack();

        updateDecks();

        sendDataToClient();
    }

    private void updateDecks() {
        for (Player player : players)
            player.updateDeck();
    }

    private void sendDataToClient() {
        //network ....
    }

    private void moveAndAttack() {
        //iterate over all units.



    }

    private void evaluateSpells() {
        List<Spell> removeSpells = new ArrayList<>();

        for (Spell spell : spells) {
            spell.decreaseTime();
            if (spell.shouldRemove()) removeSpells.add(spell);
        }

        for (Spell spell : removeSpells)
            spells.remove(spell);
    }

    private void applyPutUnits() {
        for (Pair<Unit, Integer> X : unitsToPut) {
            map.putUnit(X.getKey(), X.getValue());
        }
    }

    private void applySpells() {
        for (Spell spell : spells) {
            spell.applyTo(map);
        }
    }

    private void readRequestsFromClient() {
        //network ....
    }
}