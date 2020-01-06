package ir.sharif.aichallenge.server.logic.exceptions;

public class NoAvailableUpgradeException extends LogicException {
    public NoAvailableUpgradeException(int playerId, String type) {
        super(String.format("No upgrade card available for type: %s", type), playerId);
    }
}
