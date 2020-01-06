package ir.sharif.aichallenge.server.logic.exceptions;

public class PutMoreThanOneUnitException extends LogicException {
    public PutMoreThanOneUnitException(int playerId) {
        super("Cannot put more than one unit in a turn.", playerId);
    }
}
