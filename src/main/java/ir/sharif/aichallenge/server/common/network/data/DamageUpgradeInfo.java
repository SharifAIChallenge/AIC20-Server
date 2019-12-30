package ir.sharif.aichallenge.server.common.network.data;

import lombok.Getter;

@Getter
public class DamageUpgradeInfo extends ClientMessageInfo {
    private final int unitId;

    public DamageUpgradeInfo(int unitId) {
        this.unitId = unitId;
    }

    @Override
    public String getType() {
        return MessageTypes.UPGRADE_DAMAGE;
    }
}
