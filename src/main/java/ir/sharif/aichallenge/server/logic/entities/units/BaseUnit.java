package ir.sharif.aichallenge.server.logic.entities.units;

import ir.sharif.aichallenge.server.logic.dto.init.ClientBaseUnit;
import ir.sharif.aichallenge.server.logic.entities.TargetType;
import ir.sharif.aichallenge.server.logic.exceptions.LogicException;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class BaseUnit {


    private static HashMap<Integer, BaseUnit> instances = new HashMap<>();

    public static BaseUnit getInstance(int type) {
        BaseUnit instance = instances.get(type);
        if (instance == null) throw new LogicException();
        return instance;
    }

    private final int type;

    private final int baseHealth;
    private final int baseDamage;
    private final int baseDamageRange;
    private final int baseSpeed;
    private final int deltaDamage;
    private final int deltaDamageRange;
    private final int AP;

    private final MoveType moveType;
    private final TargetType targetType;

    public BaseUnit(int type, int baseHealth,int baseDamage, int deltaDamage, int baseDamageRange, int deltaDamageRange, int baseSpeed, int AP, MoveType moveType, TargetType targetType) {
        this.type = type;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
        this.baseDamageRange = baseDamageRange;
        this.baseSpeed = baseSpeed;
        this.moveType = moveType;
        this.targetType = targetType;
        this.deltaDamage = deltaDamage;
        this.deltaDamageRange = deltaDamageRange;
        this.AP = AP;
    }

    public static BaseUnit initBaseUnits(ClientBaseUnit cBU, int deltaDamage, int deltaDamageRange) {

        MoveType moveType;
        if(cBU.isFlying()) moveType = MoveType.AIR;
        else moveType = MoveType.GROUND;

        TargetType targetType;
        if(cBU.getTarget().equals("GROUND")) targetType = TargetType.GROUND;
        else if(cBU.getTarget().equals("AIR")) targetType = TargetType.AIR;
        else targetType = TargetType.BOTH;

        BaseUnit baseUnit = new BaseUnit(cBU.getTypeId(), cBU.getMaxHP(),
                cBU.getBaseAttack(), deltaDamage, cBU.getBaseRange(), deltaDamageRange, 1, 10,  moveType, targetType);
        //TODO AP, Multiple

        instances.put(cBU.getTypeId(), baseUnit);

        return baseUnit;
    }

}
