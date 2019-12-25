package ir.sharif.aichallenge.server.logic.entities;

import ir.sharif.aichallenge.server.logic.entities.spells.Spell;
import ir.sharif.aichallenge.server.logic.entities.units.BaseUnit;
import ir.sharif.aichallenge.server.logic.exceptions.APNotEnoughException;
import ir.sharif.aichallenge.server.logic.exceptions.SpellNotHaveException;
import ir.sharif.aichallenge.server.logic.exceptions.UnitNotInHandException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class Player {

    private static final int HAND_SIZE = 6, DECK_SIZE = 9;

    private int id, AP;

    private HashMap<Integer, Integer> spellCount = new HashMap<>();

    private ArrayList<BaseUnit> deck;
    private ArrayList<BaseUnit> hand = new ArrayList<>();
    private int[] numberOfUse = new int[DECK_SIZE];

    HashMap<BaseUnit, Integer> baseUnitId = new HashMap<>();

    BaseUnit currentPutUnit;

    public Player(int id, int AP, ArrayList<BaseUnit> deck) {
        this.id = id;
        this.AP = AP;
        this.deck = deck;
        for (int i=0; i<HAND_SIZE; i++)
            hand.add(deck.get(i));

        for (int i=0; i<DECK_SIZE; i++) numberOfUse[i] = 0;

        for (int i=0; i<DECK_SIZE; i++)
            baseUnitId.put(deck.get(i), i);
    }

    public void putUnit(BaseUnit baseUnit) {

        currentPutUnit = null;

        if (!hand.contains(baseUnit)) throw new UnitNotInHandException();
        if(AP < baseUnit.getAP()) throw new APNotEnoughException();

        numberOfUse[baseUnitId.get(baseUnit)] ++;

        currentPutUnit = baseUnit;
        AP -= baseUnit.getAP();

    }

    public void castSpell(int type) {
        int currentCount = getSpellCount(type);
        if(currentCount == 0)
            throw new SpellNotHaveException();
        currentCount --;
        spellCount.put(type, currentCount);
    }

    public void addSpell(int type) {
        int currentCount = getSpellCount(type);
        currentCount ++;
        spellCount.put(type, currentCount);
    }

    private int getSpellCount(int type) {
        if(spellCount.get(type) == null)
            return 0;
        return spellCount.get(type);
    }

    public void updateHand() {

        ArrayList<BaseUnit> chances = new ArrayList<>();
        ArrayList<Double> probs = new ArrayList<>();
        double normalize = 0;

        if(currentPutUnit == null) return ;
        for (BaseUnit baseUnit : deck) {
            if (hand.contains(baseUnit)) continue;
            chances.add(baseUnit);
            double prob = (double)1 / (1 + numberOfUse[baseUnitId.get(baseUnit)]);
            probs.add(prob);
            normalize += prob;
        }

        double random = Math.random() * normalize;

        int ptr = 2;

        for (int i=0; i<3; i++){
            if(random <= probs.get(i)) {
                ptr = i;
                break ;
            }
            random -= probs.get(i);
        }

        hand.add(chances.get(ptr));

        hand.remove(currentPutUnit);

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
