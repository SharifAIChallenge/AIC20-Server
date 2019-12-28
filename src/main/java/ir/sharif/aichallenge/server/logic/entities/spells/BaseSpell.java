package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.entities.TargetType;
import ir.sharif.aichallenge.server.logic.entities.units.BaseUnit;
import ir.sharif.aichallenge.server.logic.entities.units.MoveType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public class BaseSpell {

    private static HashMap<Integer, BaseSpell> instances = new HashMap<>();

    public static BaseSpell getInstance(int type) {
        BaseSpell instance = instances.get(type);
        if (instance == null) {
            //create one or read from file or ...
            instance = new BaseSpell(type, 0, 10, 100, SpellTargetType.ALLIED, 100);
            instances.put(type, instance);
        }
        return instance;
    }


    private int type;
    private int priority;   //todo set this
    private int duration;
    private int range;
    private SpellTargetType targetType;
    private int power;
}
