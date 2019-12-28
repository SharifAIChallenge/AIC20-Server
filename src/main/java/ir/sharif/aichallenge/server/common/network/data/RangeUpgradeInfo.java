package ir.sharif.aichallenge.server.common.network.data;

import lombok.Getter;
@Getter

public class RangeUpgradeInfo extends ClientMessageInfo {

    private final int unitId;

    public RangeUpgradeInfo(int unitId) {
        this.unitId = unitId;
    }

    public String getType() {
        return MessageTypes.UPGRADE_RANGE;
    }
}
