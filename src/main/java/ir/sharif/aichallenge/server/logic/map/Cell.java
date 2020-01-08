package ir.sharif.aichallenge.server.logic.map;

import ir.sharif.aichallenge.server.logic.dto.client.ClientCell;

import java.util.Objects;

public class Cell {
    private final int row;
    private final int col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Cell(ClientCell clientCell) {
        this.row = clientCell.getRow();
        this.col = clientCell.getCol();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return row == cell.row &&
                col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", row, col);
    }
}
