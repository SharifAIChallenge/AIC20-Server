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
                if (player.getTeam() == 0)
                    return new TeleportSpell(id, player, position, unitId,
                            new PathCell(path, player.getTeam() == 0, path.getLength() - path.getCells().indexOf(position) - 1));
                else
                    return new TeleportSpell(id, player, position, unitId,
                            new PathCell(path, player.getTeam() == 0, path.getCells().indexOf(position)));
            case DUPLICATE:
                return new DuplicateSpell(id, player, position);
            default:
                return null;
        }
    }
}
