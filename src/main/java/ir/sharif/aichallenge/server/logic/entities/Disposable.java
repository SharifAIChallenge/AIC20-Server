package ir.sharif.aichallenge.server.logic.entities;

public interface Disposable {   //todo remove this!!!!!
    void decreaseRemainingTurns();

    int getRemainingTurns();

    default boolean isDisposed() {
        return getRemainingTurns() == 0;
    }
}
