package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.dto.graphic.GraphicCell;
import ir.sharif.aichallenge.server.logic.entities.Entity;
import ir.sharif.aichallenge.server.logic.entities.Player;
import ir.sharif.aichallenge.server.logic.entities.spells.Spell;
import ir.sharif.aichallenge.server.logic.entities.units.King;
import ir.sharif.aichallenge.server.logic.entities.units.KingUnit;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnEvent {
    private boolean isAlive;
    private int ap;
    private int hp;
    private List<Integer> hand;
    private List<GraphicUnit> units;
    private List<MapSpell> mapSpells;

    public static TurnEvent getGraphicTurnEvent(Player player, Game game) {
        TurnEvent turnEvent = new TurnEvent();
        King king = game.getKingWithId(player.getId());

        assert (king.getMainUnit().getPlayer().getId() == player.getId());

        turnEvent.setAlive(king.getHealth() > 0);
        turnEvent.setAp(player.getAp());
        turnEvent.setHand(player.getHandIds());
        turnEvent.setHp(king.getHealth());
        turnEvent.setUnits(getGraphicUnits(player, game));
        turnEvent.setMapSpells(getGraphicMapSpells(player, game));

        return turnEvent;
    }

    private static List<GraphicUnit> getGraphicUnits(Player player, Game game) {
        ArrayList<GraphicUnit> graphicUnits = new ArrayList<>();
        for (Unit unit : game.getUnitsWithId().values()) {
            if(unit.getPlayer().getId() != player.getId()) continue ;
            if(unit instanceof KingUnit) continue ;
            GraphicUnit graphicUnit = GraphicUnit.getGraphicUnit(unit);
            graphicUnits.add(graphicUnit);
        }
        return graphicUnits;
    }

    private static List<MapSpell> getGraphicMapSpells(Player player, Game game) {
        ArrayList<MapSpell> mapSpells = new ArrayList<>();

        for (Spell spell : game.getSpells()) {
            if(spell.getPlayer().getId() != player.getId()) continue ;
            MapSpell mapSpell = getMapSpell(spell);
            mapSpells.add(mapSpell);
        }

        return mapSpells;
    }

    private static MapSpell getMapSpell(Spell spell) {
        MapSpell mapSpell = new MapSpell();
//        mapSpell.setCenter(new GraphicCell(spell.getPosition().getRow(), spell.getPosition().getCol()));
//        mapSpell.setRange(spell.getRange());
        mapSpell.setTypeId(spell.getBaseSpell().getTypeId());
        mapSpell.setSpellId(spell.getId());
        mapSpell.setUnitIds(spell.getCaughtUnits().stream().map(Entity::getId).collect(Collectors.toList()));
        return mapSpell;
    }
}
