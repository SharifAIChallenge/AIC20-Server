package ir.sharif.aichallenge.server.logic.exceptions;

public class TeleportUnitIdException extends LogicException {
    public TeleportUnitIdException(int unitId, int playerId) {
        super(String.format("unit with id = " + unitId + " is not in map or for other player"), playerId);
    }
}
