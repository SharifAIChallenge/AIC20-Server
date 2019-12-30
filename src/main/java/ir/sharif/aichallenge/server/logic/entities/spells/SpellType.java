package ir.sharif.aichallenge.server.logic.entities.spells;

public enum SpellType {
    DAMAGE(0, 2), POISON(0, 6), HEAL(1, 3),
    TELE(2, 4), HASTE(3, 1), DUPLICATE(4, 5);

    private int priority;
    private int typeId;

    private SpellType(int priority, int typeId) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public int getTypeId() {
        return typeId;
    }

    public static SpellType getSpellTypeByTypeId(int typeId) {
        for (SpellType spellType: SpellType.values()) {
            if (spellType.getTypeId() == typeId)
                return spellType;
        }
        return null;
    }
}
