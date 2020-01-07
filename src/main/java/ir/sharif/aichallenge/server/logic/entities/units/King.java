package ir.sharif.aichallenge.server.logic.entities.units;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.TargetType;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Path;
import ir.sharif.aichallenge.server.logic.map.PathCell;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class King {

    private static int KING_TYPE_1 = 100, KING_TYPE_2 = 101;

    private ArrayList<KingUnit> units = new ArrayList<>();

    private KingUnit.HealthComponent healthComponent = new KingUnit.HealthComponent();

    @Getter
    private KingUnit mainUnit;

    public King(Player player, Cell center, int health, int damage, int range) {
        //TODO: cleanup
        healthComponent.setHealth(health);

        for (int dr = -1; dr <= 1; dr++)
            for (int dc = -1; dc <= 1; dc++) {
                int nr = center.getRow() + dr, nc = center.getCol() + dc;

                BaseUnit baseUnit;

                if (dr == 0 && dc == 0) {
                    baseUnit = new BaseUnit(KING_TYPE_1, 0, damage, 0, range, 0, 0, false, 0, MoveType.BOTH, TargetType.BOTH);
                } else {
                    baseUnit = new BaseUnit(KING_TYPE_2, 0, 0, 0, 0, 0, 0, false, 0, MoveType.BOTH, TargetType.NONE);
                }

                KingUnit kingUnit = new KingUnit(baseUnit, player, healthComponent);
                kingUnit.setPosition(new PathCell(new Path(-1, new Cell(nr, nc)), false, 0));
                units.add(kingUnit);
                if (dr == 0 && dc == 0)
                    mainUnit = kingUnit;
            }
    }

    public List<KingUnit> getUnits() {
        return units;
    }

    public int getHealth() {
        return healthComponent.getHealth();
    }

    public boolean isAlive() {
        return getMainUnit().isAlive();
    }

}
