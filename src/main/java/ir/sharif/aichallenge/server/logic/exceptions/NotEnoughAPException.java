package ir.sharif.aichallenge.server.logic.exceptions;

public class NotEnoughAPException extends LogicException {
    public NotEnoughAPException(int playerId, int needed, int current) {
        super(String.format("AP is not enough. Needed: %d, Current: %d", needed, current), playerId);
    }
}
