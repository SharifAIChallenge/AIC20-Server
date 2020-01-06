package ir.sharif.aichallenge.server.logic.exceptions;

public class UseMoreThanOneSpellException extends LogicException {
    public UseMoreThanOneSpellException(int playerId) {
        super("Cannot cast more than one spell in a turn.", playerId);
    }
}
