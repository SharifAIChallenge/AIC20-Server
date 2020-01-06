package ir.sharif.aichallenge.server.logic.exceptions;

import lombok.Getter;

public class LogicException extends Exception {

    @Getter
    private int playerId = -1;

    public LogicException() {
    }

    public LogicException(String message) {
        super(message);
    }

    public LogicException(String message, int playerId) {
        super(message);
        this.playerId = playerId;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Player id: " + playerId;
    }
}
