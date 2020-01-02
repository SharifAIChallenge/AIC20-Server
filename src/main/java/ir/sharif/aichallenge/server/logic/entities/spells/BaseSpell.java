package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.dto.client.init.ClientSpell;
import ir.sharif.aichallenge.server.logic.exceptions.LogicException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public class BaseSpell {

    private static HashMap<Integer, BaseSpell> instances = new HashMap<>();

    private static HashMap<Integer, SpellType> typeIdsToTypes = new HashMap<>();
    private static HashMap<SpellType, Integer> typesToTypeIds = new HashMap<>();

    public static BaseSpell getInstance(SpellType type) {
        return getInstance(typesToTypeIds.get(type));
    }

    public static BaseSpell getInstance(int typeId) {
        BaseSpell instance = instances.get(typeId);
        if (instance == null) throw new LogicException();
        return instance;
    }

    public static void initSpell(ClientSpell clientSpell) {
        typeIdsToTypes.put(clientSpell.getTypeId(), clientSpell.getType());
        typesToTypeIds.put(clientSpell.getType(), clientSpell.getTypeId());

        instances.put(clientSpell.getTypeId(), new BaseSpell(clientSpell.getType(),
                clientSpell.getTypeId(),
                clientSpell.getPriority(),
                clientSpell.getDuration(),
                clientSpell.getRange(),
                clientSpell.getTarget(),
                clientSpell.getPower()));
    }

    public static SpellType getTypeByTypeId(int typeId) {
        return typeIdsToTypes.get(typeId);
    }

    private SpellType type;
    private int typeId;
    private int priority;
    private int duration;
    private int range;
    private SpellTargetType targetType;
    private int power;
}
