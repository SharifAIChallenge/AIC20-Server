package ir.sharif.aichallenge.server.logic.dto.client.turn;

import lombok.*;

import java.util.List;

@Builder
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
