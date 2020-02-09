package ir.sharif.aichallenge.server.logic.exceptions;

public class UnitIsKingException extends LogicException {
    public UnitIsKingException(int playerId, int unitId) {
        super(String.format("Unit is King: unitId %d", unitId), playerId);
    }
}
