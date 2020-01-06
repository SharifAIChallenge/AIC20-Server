package ir.sharif.aichallenge.server.logic.exceptions;

public class SpellNotHaveException extends LogicException {
    public SpellNotHaveException(int playerId, int type) {
        super(String.format("Player doesn't have spell. Type: %d", type), playerId);
    }
}
