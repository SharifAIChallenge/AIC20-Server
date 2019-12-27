package ir.sharif.aichallenge.server.common.network.data;

import ir.sharif.aichallenge.server.logic.map.Cell;
import lombok.Getter;

@Getter
public class CastSpellInfo extends ClientMessageInfo {

    private int typeId;
    private Cell cell;
    private int unitId;
    private int pathId;

    @Override
    public String getType() {
        return MessageTypes.CAST_SPELL;
    }


}
