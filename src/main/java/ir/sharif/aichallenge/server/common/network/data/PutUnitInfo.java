package ir.sharif.aichallenge.server.common.network.data;

public class PutUnitInfo extends ClientMessageInfo {
    private final int unitId;
    private final int pathId;

    public PutUnitInfo(int unitId, int pathId) {
        this.unitId = unitId;
        this.pathId = pathId;
    }

    @Override
    public String getType() {
        return MessageTypes.PUT;
    }
}
