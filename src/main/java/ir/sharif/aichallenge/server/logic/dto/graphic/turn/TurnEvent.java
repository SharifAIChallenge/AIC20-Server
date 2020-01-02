package ir.sharif.aichallenge.server.logic.dto.graphic.turn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
}
