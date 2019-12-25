package ir.sharif.aichallenge.server.logic.entities;

public interface Disposable {
    void decreaseRemainingTurns();

    int getRemainingTurns();

    default boolean isDisposed() {
        return getRemainingTurns() == 0;
    }
}
