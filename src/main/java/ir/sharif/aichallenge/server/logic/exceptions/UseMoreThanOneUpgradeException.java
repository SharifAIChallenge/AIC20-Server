package ir.sharif.aichallenge.server.logic.exceptions;

public class UseMoreThanOneUpgradeException extends LogicException {
    public UseMoreThanOneUpgradeException(int playerId) {
        super("Cannot upgrade more than one time in a turn.", playerId);
    }
}
