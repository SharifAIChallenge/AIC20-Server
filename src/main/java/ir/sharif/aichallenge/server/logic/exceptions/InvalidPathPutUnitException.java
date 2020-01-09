package ir.sharif.aichallenge.server.logic.exceptions;

public class InvalidPathPutUnitException extends LogicException {
    public InvalidPathPutUnitException(int pathId) {
        super(String.format("Put unit is not valid path %d" ,pathId));
    }
}
