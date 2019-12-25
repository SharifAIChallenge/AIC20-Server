package ir.sharif.aichallenge.server.logic.map;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.*;

public class Path {
    @Getter
    private final int id;
    private final ArrayList<Cell> cells;

    public Path(int id, List<Cell> cells) {
        this.id = id;
        if (cells.isEmpty())
            throw new IllegalArgumentException("Path cannot be empty.");
        this.cells = new ArrayList<>(cells);
    }

    public Path(int id, Cell... cells) {
        this(id, Arrays.asList(cells));
    }

    public Cell getCellAt(int index) {
        return this.cells.get(index);
    }

    public int getLength() {
        return cells.size();
    }

    public List<Cell> getCells() {
        return Collections.unmodifiableList(cells);
    }
}
