package ir.sharif.aichallenge.server.logic.dto.client.init;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameConstants {
    private int maxAP;
    private int maxTurns;
    private int turnTimeout;
    private int pickTimeout;
    private int turnsToUpgrade;
    private int turnsToSpell;
    private int damageUpgradeAddition;
    private int rangeUpgradeAddition;
}
