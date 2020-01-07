package ir.sharif.aichallenge.server.logic.dto.client.turn;

import ir.sharif.aichallenge.server.logic.dto.graphic.turn.TurnAttack;
import ir.sharif.aichallenge.server.logic.entities.units.King;
import ir.sharif.aichallenge.server.logic.entities.units.KingUnit;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnKing {
    private int playerId;
    private boolean isAlive;
    private int hp;
    private int target;

}
