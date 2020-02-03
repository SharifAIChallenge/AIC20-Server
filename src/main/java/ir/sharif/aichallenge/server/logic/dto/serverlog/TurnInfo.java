package ir.sharif.aichallenge.server.logic.dto.serverlog;

import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnCastSpell;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnKing;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnUnit;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.TurnAttack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TurnInfo {
    private int turnNum;
    private List<TurnKing> kings;
    private List<TurnUnit> units;
    private List<TurnUnit> diedUnits;
    private List<Integer> putUnits;
    private List<Integer> rangeUpgradedUnits;
    private List<Integer> damageUpgradedUnits;
    private List<TurnCastSpell> castSpells;
    private List<TurnAttack> attacks;
    private List<PlayerInfo> players;
}
