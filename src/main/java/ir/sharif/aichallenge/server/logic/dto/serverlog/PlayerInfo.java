package ir.sharif.aichallenge.server.logic.dto.serverlog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerInfo {
    private List<Integer> deck;
    private List<Integer> hand;
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
