package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Path;
import ir.sharif.aichallenge.server.logic.map.PathCell;

public class SpellFactory {

    private static int id = 0;

    public static Spell createSpell(int typeId, Player player, Cell cell, int unitId, Path path) {
        id++;
        switch (typeId) {
            case HasteSpell.TYPE:
                return new HasteSpell(id, player, cell);
            case DamageSpell.TYPE:
                return new DamageSpell(id, player, cell);
            case HealSpell.TYPE:
                return new HealSpell(id, player, cell);
            case TeleportSpell.TYPE:
                return new TeleportSpell(id, player, cell, unitId,
                        new PathCell(path, player.getTeam() == 0, path.getCells().indexOf(cell)));
            case DuplicateSpell.TYPE:
                return new DuplicateSpell(id, player, cell);
            case PoisonSpell.TYPE:
                return new PoisonSpell(id, player, cell);
            default:
                return null;
        }
    }
}
