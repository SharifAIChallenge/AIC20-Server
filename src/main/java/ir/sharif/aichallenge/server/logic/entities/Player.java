package ir.sharif.aichallenge.server.logic.entities;

import ir.sharif.aichallenge.server.logic.entities.units.BaseUnit;
import ir.sharif.aichallenge.server.logic.exceptions.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Player {

    private static final int HAND_SIZE = 6, DECK_SIZE = 9;
    private int id;
    private int ap;
    private int maxAP;

    private Map<Integer, Integer> spellCount = new HashMap<>();

    private List<BaseUnit> deck = new ArrayList<>();
    private List<BaseUnit> hand = new ArrayList<>();
    private int[] numberOfUse = new int[DECK_SIZE];

    private int numberOfDamageUpgrades = 0;
    private int numberOfRangeUpgrades = 0;

    @Setter
    private boolean upgradeUsed, spellUsed, putUsed;
    @Setter
    private boolean deckInit;

    private Map<BaseUnit, Integer> baseUnitId = new HashMap<>();

    private BaseUnit currentPutUnit;

    public Player(int id, int ap) {
        this.id = id;
        this.ap = ap;
        this.maxAP = ap;

        upgradeUsed = putUsed = spellUsed = false;
        deckInit = false;
    }

    public void initDeck(List<Integer> baseUnitIds, int numberOfBaseUnits) {
        setDeckInit(true);

        ArrayList<Integer> validIds = new ArrayList<>();
        for (Integer id : baseUnitIds) {
            if(validIds.size() >= DECK_SIZE) break;
            if (id >= 0 && id < numberOfBaseUnits && !validIds.contains(id))
                validIds.add(id);
        }

        ArrayList<Integer> ids = new ArrayList<>(validIds);

        while (ids.size() < DECK_SIZE) {
            int random_id = getRandom(0, numberOfBaseUnits);
            if (ids.contains(random_id)) continue;
            ids.add(random_id);
        }

        for (Integer id : ids) {
            if (this.deck.size() == DECK_SIZE) break;
            this.deck.add(BaseUnit.getInstance(id));    //TODO: may throw null pointer exception
        }

        for (int i = 0; i < HAND_SIZE; i++)
            hand.add(deck.get(i));

        for (int i = 0; i < DECK_SIZE; i++) numberOfUse[i] = 0;

        for (int i = 0; i < DECK_SIZE; i++)
            baseUnitId.put(deck.get(i), i);

    }

    public void addUpgradeRangeToken() {
        numberOfRangeUpgrades++;
    }

    public void addUpgradeDamageToken() {
        numberOfDamageUpgrades++;
    }

    public void useUpgradeDamage() throws LogicException {
        if (upgradeUsed) throw new UseMoreThanOneUpgradeException(id);
        if (numberOfDamageUpgrades == 0) throw new NoAvailableUpgradeException(id, "Damage");
        numberOfDamageUpgrades--;
        setUpgradeUsed(true);
    }

    public void useUpgradeRange() throws LogicException {
        if (upgradeUsed) throw new UseMoreThanOneUpgradeException(id);
        if (numberOfRangeUpgrades == 0) throw new NoAvailableUpgradeException(id, "Range");
        numberOfRangeUpgrades--;
        setUpgradeUsed(true);
    }

    public void checkPutUnit(BaseUnit baseUnit) throws LogicException {
        currentPutUnit = null;

        if (!hand.contains(baseUnit)) throw new UnitNotInHandException(id, baseUnit.getType());
        if (ap < baseUnit.getCost()) throw new NotEnoughAPException(id, baseUnit.getCost(), ap);
        if (putUsed) throw new PutMoreThanOneUnitException(id);

    }

    public void putUnit(BaseUnit baseUnit) {
        setPutUsed(true);

        numberOfUse[baseUnitId.get(baseUnit)]++;

        currentPutUnit = baseUnit;
        ap -= baseUnit.getCost();
    }

    public void checkSpell(int type) throws LogicException{
        int currentCount = getSpellCountOfType(type);
        if (currentCount == 0)
            throw new SpellNotHaveException(id, type);

        if (spellUsed)
            throw new UseMoreThanOneSpellException(id);

    }

    public void castSpell(int type) {
        int currentCount = getSpellCountOfType(type);

        setSpellUsed(true);

        currentCount--;
        spellCount.put(type, currentCount);
    }

    public void unCastSpell(int type) {
        int currentCount = getSpellCountOfType(type);
        currentCount++;
        spellCount.put(type, currentCount);
    }

    public void addSpell(int type) {
        int currentCount = getSpellCountOfType(type);
        currentCount++;
        spellCount.put(type, currentCount);
    }

    private int getSpellCountOfType(int type) {
        if (spellCount.get(type) == null)
            return 0;
        return spellCount.get(type);
    }

    public void reset() {
        setPutUsed(false);
        setSpellUsed(false);
        setUpgradeUsed(false);

        updateHand();
    }

    private void updateHand() {

        if (currentPutUnit == null) return;

        List<BaseUnit> chances = new ArrayList<>();
        List<Double> probs = new ArrayList<>();
        double normalize = 0;

        for (BaseUnit baseUnit : deck) {
            if (hand.contains(baseUnit)) continue;
            chances.add(baseUnit);
            double prob = (double) 1 / (1 + numberOfUse[baseUnitId.get(baseUnit)]);
            probs.add(prob);
            normalize += prob;
        }

        double random = Math.random() * normalize;

        int ptr = 2;

        for (int i = 0; i < 3; i++) {
            if (random <= probs.get(i)) {
                ptr = i;
                break;
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

    public boolean isAlly(int otherId) {
        return this.id == otherId || this.id == (otherId ^ 2);
    }

    public boolean isAllyExclusive(Player other) {
        return this.getId() != other.getId() && this.getTeam() == other.getTeam();
    }

    private int getRandom(int L, int R) { //[L, R)
        return (int) (Math.random() * (R - L)) + L;
    }

    public List<Integer> getDeckIds() {
        List<Integer> ids = new ArrayList<>();
        for (BaseUnit baseUnit : this.deck)
            ids.add(baseUnit.getType());
        return ids;
    }

    public List<Integer> getHandIds() {
        List<Integer> ids = new ArrayList<>();
        for (BaseUnit baseUnit : this.hand)
            ids.add(baseUnit.getType());
        return ids;
    }

    public List<Integer> getAvailableSpellIds() {
        List<Integer> availableSpells = new ArrayList<>();
        for (int spellId : spellCount.keySet()) {
            for (int i = 0; i < spellCount.get(spellId); i++) {
                availableSpells.add(spellId);
            }
        }
        return availableSpells;
    }

    public boolean getDeckInit() {
        return deckInit;
    }

}
