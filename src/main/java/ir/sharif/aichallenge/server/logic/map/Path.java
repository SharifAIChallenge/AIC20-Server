package ir.sharif.aichallenge.server.logic.map;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.*;

public class Path {
    @Getter
    private final int id;
    private final ArrayList<Cell> cells;

    private final int[] kingPlacesIndices;

    public Path(int id, List<Cell> cells) {
        this(id, cells, null);
    }

    public Path(int id, List<Cell> cells, int[] kingPlacesIndices) {
        this.id = id;
        if (cells.isEmpty())
            throw new IllegalArgumentException("Path cannot be empty.");
        this.cells = new ArrayList<>(cells);
        this.kingPlacesIndices = kingPlacesIndices == null ? null : Arrays.copyOf(kingPlacesIndices, kingPlacesIndices.length);
    }

    public Path(int id, Cell... cells) {
        this(id, Arrays.asList(cells));
    }

    public Cell getCellAt(int index) {
        return this.cells.get(index);
    }

    public Cell getFirst() {
        return getCellAt(0);
    }

    public Cell getLast() {
        return getCellAt(getLength() - 1);
    }

    public int getLength() {
        return cells.size();
    }

    public List<Cell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    public int getIndexForKing(int kingId) {
        return kingPlacesIndices[kingId];
    }

    public boolean shouldReverseForTeam(int team) {
        return getIndexForKing(team) != 0 && getIndexForKing(team ^ 2) != 0;
    }

    public Path reverse() {
        List<Cell> reversed = new ArrayList<>(getLength());
        for (int i = getCells().size() - 1; i >= 0; i--)
            reversed.add(getCellAt(i));
        return new Path(id, reversed);
    }

    public String getFullView(int width, int height) {
        boolean[][] map = new boolean[height][width];
        for (int i = 0; i < getLength(); i++) {
            final Cell cell = getCellAt(i);
            map[cell.getRow()][cell.getCol()] = true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("―");
        for (int k = 0; k < width; k++)
            sb.append("――");
        sb.append('\n');
        for (int i = 0; i < height; i++) {
            sb.append('│');
            for (int j = 0; j < width; j++) {
                sb.append(map[i][j] ? '#' : ' ').append('│');
            }
            sb.append('\n');
            for (int k = 0; k < width; k++)
                sb.append("――");
            sb.append('\n');
        }
        return sb.toString();
    }
}
