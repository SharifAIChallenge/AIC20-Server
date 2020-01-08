package ir.sharif.aichallenge.server.logic.entities.spells;

import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.map.Cell;
import ir.sharif.aichallenge.server.logic.map.Path;
import ir.sharif.aichallenge.server.logic.map.PathCell;

public class SpellFactory {

    private static int id = 0;

    public static Spell createSpell(int typeId, Player player, Cell position, int unitId, Path path) {
        id++;
        switch (BaseSpell.getTypeByTypeId(typeId)) {
            case HP:
                return new HPSpell(id, BaseSpell.getInstance(typeId), player, position);
            case HASTE:
                return new HasteSpell(id, player, position);
            case TELE:
                return new TeleportSpell(id, player, position, unitId,
                        PathCell.createPathCell(path, path.shouldReverseForTeam(player.getTeam()), position));

            case DUPLICATE:
                return new DuplicateSpell(id, player, position);
            default:
                return null;
        }
    }
}
