package ir.sharif.aichallenge.server.logic.entities.units;

import ir.sharif.aichallenge.server.logic.dto.client.init.ClientBaseUnit;
import ir.sharif.aichallenge.server.logic.entities.TargetType;
import ir.sharif.aichallenge.server.logic.exceptions.LogicException;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Objects;

@Getter
@ToString
public class BaseUnit {
    private static HashMap<Integer, BaseUnit> instances = new HashMap<>();

    public static BaseUnit getInstance(int type) {
        return instances.get(type);
    }

    public static BaseUnit initBaseUnits(ClientBaseUnit cBU, int deltaDamage, int deltaDamageRange) {

        MoveType moveType;
        if (cBU.isFlying()) moveType = MoveType.AIR;
        else moveType = MoveType.GROUND;

        TargetType targetType;
        if (cBU.getTarget().equals("GROUND")) targetType = TargetType.GROUND;
        else if (cBU.getTarget().equals("AIR")) targetType = TargetType.AIR;
        else targetType = TargetType.BOTH;

        BaseUnit baseUnit = new BaseUnit(cBU.getTypeId(), cBU.getMaxHP(),
                1, cBU.getBaseAttack(), cBU.getBaseRange(), deltaDamage, deltaDamageRange, cBU.isMultiple(),
                cBU.getAp(), moveType, targetType);

        instances.put(cBU.getTypeId(), baseUnit);

        return baseUnit;
    }

    private final int type;
    private final int baseHealth;
    private final int baseSpeed;
    private final int baseDamage;
    private final int baseDamageRange;
    private final int deltaDamage;
    private final int deltaDamageRange;
    private final boolean isMultiTarget;

    private final int cost;
    private final MoveType moveType;

    private final TargetType targetType;

    public BaseUnit(int type, int baseHealth, int baseSpeed,
                    int baseDamage, int baseDamageRange, int deltaDamage, int deltaDamageRange, boolean isMultiTarget,
                    int cost, MoveType moveType, TargetType targetType) {
        this.type = type;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
        this.baseDamageRange = baseDamageRange;
        this.baseSpeed = baseSpeed;
        this.deltaDamage = deltaDamage;
        this.deltaDamageRange = deltaDamageRange;
        this.isMultiTarget = isMultiTarget;
        this.cost = cost;
        this.moveType = moveType;
        this.targetType = targetType;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BaseUnit)) return false;
        return ((BaseUnit) obj).type == this.type;
    }
}
