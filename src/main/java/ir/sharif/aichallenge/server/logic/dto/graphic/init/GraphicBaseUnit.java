package ir.sharif.aichallenge.server.logic.dto.graphic.init;

import ir.sharif.aichallenge.server.logic.dto.client.init.ClientBaseUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphicBaseUnit {
    private int typeId;
    private int maxHP;
    private int ap;


    public static GraphicBaseUnit makeGraphicBaseUnit(ClientBaseUnit clientBaseUnit) {
        return new GraphicBaseUnit(clientBaseUnit.getTypeId(),
                clientBaseUnit.getMaxHP(), clientBaseUnit.getAp());
    }
}
