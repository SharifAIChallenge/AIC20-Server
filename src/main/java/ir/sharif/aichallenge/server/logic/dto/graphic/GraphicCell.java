package ir.sharif.aichallenge.server.logic.dto.graphic;

import ir.sharif.aichallenge.server.logic.dto.client.ClientCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphicCell {
    private int row;
    private int col;

    public static GraphicCell makeGraphicCell(ClientCell clientCell) {
        return new GraphicCell(clientCell.getRow(), clientCell.getCol());
    }
}
