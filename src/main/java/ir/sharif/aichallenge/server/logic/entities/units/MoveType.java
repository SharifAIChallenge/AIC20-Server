package ir.sharif.aichallenge.server.logic.entities.units;

public enum MoveType {
    GROUND(1),
    AIR(2);

    public final int value;

    MoveType(int value) {
        this.value = value;
    }
}
