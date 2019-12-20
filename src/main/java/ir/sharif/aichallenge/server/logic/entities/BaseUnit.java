package ir.sharif.aichallenge.server.logic.entities;

import lombok.Getter;

import java.util.HashMap;
import java.util.Objects;

@Getter
public class BaseUnit {

    private static class BaseInfo {
        public final int type;
        public final int level;

        public BaseInfo(int type, int level) {
            this.type = type;
            this.level = level;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BaseInfo)) return false;
            BaseInfo baseInfo = (BaseInfo) o;
            return type == baseInfo.type &&
                    level == baseInfo.level;
        }

        @Override
        public int hashCode() {
            return type << (Integer.SIZE / 2) + level;
        }
    }

    private static HashMap<BaseInfo, BaseUnit> instances = new HashMap<>();

    public static BaseUnit getInstance(int type, int level) {
        BaseUnit instance = instances.get(new BaseInfo(type, level));
        if (instance == null) {
            //create one or read from file or ...
            instance = new BaseUnit(type, level, 0, 0, 0, MoveType.GROUND, TargetType.BOTH);
        }

        return instance;
    }

    private final int type;
    private final int level;

    private final int baseHealth;
    private final int baseDamage;
    private final int baseSpeed;

    private final MoveType moveType;
    private final TargetType targetType;

    public BaseUnit(int type, int level, int baseHealth, int baseDamage, int baseSpeed, MoveType moveType, TargetType targetType) {
        this.type = type;
        this.level = level;
        this.baseHealth = baseHealth;
        this.baseDamage = baseDamage;
        this.baseSpeed = baseSpeed;
        this.moveType = moveType;
        this.targetType = targetType;
    }
}
