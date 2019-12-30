package ir.sharif.aichallenge.server.logic.dto;

import ir.sharif.aichallenge.server.logic.map.Cell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientCell {
    private int row;
    private int col;

    public ClientCell(Cell cell) {
        row = cell.getRow();
        col = cell.getCol();
    }
}
