package ir.sharif.aichallenge.server.logic.entities;

public enum TargetType {
    NONE(0),
    GROUND(1),
    AIR(2),
    BOTH(3);

    public final int value;

    TargetType(int value) {
        this.value = value;
    }
}
