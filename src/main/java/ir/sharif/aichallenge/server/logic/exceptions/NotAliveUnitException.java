package ir.sharif.aichallenge.server.logic.exceptions;

public class NotAliveUnitException extends LogicException {
    public NotAliveUnitException(int unitId) {
        super(String.format("Using a unit which is not alive. unit id: %d", unitId));
    }
}
