package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import java.util.List;

public class TurnEvent {
    private boolean isAlive;
    private int ap;
    private int hp;
    private List<Integer> hand;
    private List<GraphicUnit> units;
    private List<MapSpell> mapSpells;
}
