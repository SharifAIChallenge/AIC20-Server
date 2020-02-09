package ir.sharif.aichallenge.server.logic.exceptions;

public class UnitNotInHandException extends LogicException {
    public UnitNotInHandException(int playerId, int typeId) {
        super(String.format("Unit is not in the hand. Type: %d", typeId), playerId);
    }
}
