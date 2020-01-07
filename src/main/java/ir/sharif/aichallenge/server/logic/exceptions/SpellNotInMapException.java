package ir.sharif.aichallenge.server.logic.exceptions;

import ir.sharif.aichallenge.server.logic.map.Cell;

public class SpellNotInMapException extends LogicException {
    public SpellNotInMapException(int playerId, Cell cell) {
        super(String.format("Spell is not in map. Target cell: %s", cell.toString()), playerId);
    }
}
