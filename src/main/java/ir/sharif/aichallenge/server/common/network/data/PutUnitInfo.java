package ir.sharif.aichallenge.server.common.network.data;

import lombok.Getter;

@Getter
public class PutUnitInfo extends ClientMessageInfo {
    private final int typeId;
    private final int pathId;

    public PutUnitInfo(int typeId, int pathId) {
        this.typeId = typeId;
        this.pathId = pathId;
    }

    @Override
    public String getType() {
        return MessageTypes.PUT_UNIT;
    }
}
