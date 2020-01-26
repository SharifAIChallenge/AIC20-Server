package ir.sharif.aichallenge.server.logic.exceptions;

import ir.sharif.aichallenge.server.logic.map.PathCell;

public class TooFarTeleportException extends LogicException {
    public TooFarTeleportException(int playerId, PathCell targetCell) {
        super(String.format("You can only teleport to the first half of the path. Target: %s", targetCell.getCell()), playerId);
    }
}
