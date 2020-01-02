package ir.sharif.aichallenge.server.logic.dto.graphic.init;

import ir.sharif.aichallenge.server.logic.dto.client.init.ClientBaseKing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraphicKing {
    private int row;
    private int col;
    private int pId;
    private int hp;
    private String name;

    public static GraphicKing makeGraphicKing(ClientBaseKing clientBaseKing) {
        GraphicKing graphicKing = new GraphicKing();
        graphicKing.setRow(clientBaseKing.getCenter().getRow());
        graphicKing.setCol(clientBaseKing.getCenter().getCol());
        graphicKing.setPId(clientBaseKing.getPlayerId());
        graphicKing.setHp(clientBaseKing.getHp());
        //todo set name
        return graphicKing;
    }
}
