package ir.sharif.aichallenge.server.logic.entities;

import ir.sharif.aichallenge.server.logic.entities.units.BaseUnit;
import ir.sharif.aichallenge.server.logic.exceptions.*;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Player {

    private int handSize, deckSize;
    private int id;
    private int ap;
    private int maxAP;

    private Map<Integer, Integer> spellCount = new HashMap<>();

    private List<BaseUnit> deck = new ArrayList<>();
    private List<BaseUnit> hand = new ArrayList<>();
    private int[] numberOfUse;

    private int numberOfDamageUpgrades = 0;
    private int numberOfRangeUpgrades = 0;

    @Setter
    private boolean upgradeUsed, spellUsed, putUsed;
    private boolean deckInit;

    @Getter
    private int receivedSpell;
    @Getter
    private boolean gotDamageUpgrade, gotRangeUpgrade;

    private Map<BaseUnit, Integer> baseUnitId = new HashMap<>();

    private BaseUnit currentPutUnit;

    public Player(int id, int ap, int handSize, int deckSize) {
        this.id = id;
        this.ap = ap;
        this.maxAP = ap;

        this.handSize = handSize;
        this.deckSize = deckSize;

        upgradeUsed = putUsed = spellUsed = false;
        deckInit = false;

        numberOfUse = new int[deckSize];
    }

    public void initDeck(List<Integer> baseUnitIds, int numberOfBaseUnits) {

        deckInit = true;

        ArrayList<Integer> validIds = new ArrayList<>();
        for (Integer id : baseUnitIds) {
            if (validIds.size() >= deckSize) break;
            if (id >= 0 && id < numberOfBaseUnits && !validIds.contains(id))
                validIds.add(id);
        }

        ArrayList<Integer> ids = new ArrayList<>(validIds);

        while (ids.size() < deckSize) {
            int random_id = getRandom(0, numberOfBaseUnits);
            if (ids.contains(random_id)) continue;
            ids.add(random_id);
        }

        for (Integer id : ids) {
            if (this.deck.size() == deckSize) break;
            this.deck.add(BaseUnit.getInstance(id));    //TODO: may throw null pointer exception
        }

        for (int i = 0; i < handSize; i++)
            hand.add(deck.get(i));

        for (int i = 0; i < deckSize; i++) numberOfUse[i] = 0;

        for (int i = 0; i < deckSize; i++)
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

    public void checkSpell(int type) throws LogicException {
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

        ap ++;
        ap = Math.min(ap, maxAP);

        updateHand();
    }

    private void updateHand() {

        if (currentPutUnit == null) return;

        hand.remove(currentPutUnit);

        List<BaseUnit> chances = new ArrayList<>();
        List<Double> probabilities = new ArrayList<>();
        double normalize = 0;

        for (BaseUnit baseUnit : deck) {
            if (hand.contains(baseUnit)) continue;
            chances.add(baseUnit);
            double prob = (double) 1 / (1 + numberOfUse[baseUnitId.get(baseUnit)]);
            probabilities.add(prob);
            normalize += prob;
        }

        double random = Math.random() * normalize;

        int ptr = chances.size() - 1;

        for (int i = 0; i < chances.size(); i++) {
            if (random <= probabilities.get(i)) {
                ptr = i;
                break;
            }
            random -= probabilities.get(i);
        }

        hand.add(chances.get(ptr));
        currentPutUnit = null;
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
        return this.deck.stream().map(BaseUnit::getType)
                .collect(Collectors.toCollection(() -> new ArrayList<>(this.deck.size())));
    }

    public List<Integer> getHandIds() {
        return this.hand.stream().map(BaseUnit::getType)
                .collect(Collectors.toCollection(() -> new ArrayList<>(this.hand.size())));
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
