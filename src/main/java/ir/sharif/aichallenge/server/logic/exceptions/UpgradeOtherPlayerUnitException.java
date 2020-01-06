package ir.sharif.aichallenge.server.logic.exceptions;

public class UpgradeOtherPlayerUnitException extends LogicException {
    public UpgradeOtherPlayerUnitException(int playerId, int targetPlayerId) {
        super(String.format("Cannot upgrade the other player's unit. Target unit player id: %d", targetPlayerId), playerId);
    }
}
