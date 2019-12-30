package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.dto.init.ClientSpell;
import ir.sharif.aichallenge.server.logic.entities.TargetType;
import ir.sharif.aichallenge.server.logic.entities.units.BaseUnit;
import ir.sharif.aichallenge.server.logic.entities.units.MoveType;
import ir.sharif.aichallenge.server.logic.exceptions.LogicException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public class BaseSpell {

    private static HashMap<Integer, BaseSpell> instances = new HashMap<>();

    public static BaseSpell getInstance(int type) {
        BaseSpell instance = instances.get(type);
        if (instance == null) throw new LogicException();
        return instance;
    }

    public static void initSpell(ClientSpell clientSpell) {
        SpellTargetType spellTargetType;
        if(clientSpell.isDamaging()) spellTargetType = SpellTargetType.ENEMY;
        else spellTargetType = SpellTargetType.ALLIED;

        BaseSpell baseSpell = new BaseSpell(clientSpell.getTypeId(), clientSpell.getTurnEffect(),
                clientSpell.getRange(), spellTargetType, clientSpell.getPower());
        instances.put(clientSpell.getTypeId(), baseSpell);
    }

    private int type;
    private int duration;
    private int range;
    private SpellTargetType targetType;
    private int power;
}
