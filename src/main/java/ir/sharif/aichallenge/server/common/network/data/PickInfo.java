package ir.sharif.aichallenge.server.common.network.data;

import lombok.Getter;

import java.util.List;

@Getter
public class PickInfo extends ClientMessageInfo {
    private final List<Integer> units;

    public PickInfo(List<Integer> units) {
        this.units = units;
    }

    @Override
    public String getType() {
        return MessageTypes.PICK;
    }
}
