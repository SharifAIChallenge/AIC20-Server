package ir.sharif.aichallenge.server.logic.dto.turn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientTurnMessage {
    private int currTurn;
    private List<Integer> deck;
    private List<Integer> hand;
    private List<TurnKing> kings;
    private List<TurnUnit> units;
    private List<TurnCastSpell> castSpells;
    private int receivedSpell;
    private int friendReceivedSpell;
    private List<Integer> mySpells;
    private List<Integer> friendSpells;
    private boolean gotRangeUpgrade;
    private boolean gotDamageUpgrade;
    private int availableRangeUpgrades;
    private int availableDamageUpgrades;
    private int remainingAP;
}
