package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.logic.dto.client.turn.ClientTurnMessage;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnCastSpell;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.TurnAttack;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public final class ClientHandler {
    private final List<TurnAttack> currentAttacks;
    private final Set<Integer> damageUpgradedUnits;
    private final Set<Integer> rangeUpgradedUnits;
    private final Set<Integer> playedUnits;
    private final ClientTurnMessage[] clientTurnMessages;
    private final List<TurnCastSpell> turnCastSpells;

    public ClientHandler(List<TurnAttack> currentAttacks, Set<Integer> damageUpgradedUnits,
                         Set<Integer> rangeUpgradedUnits, Set<Integer> playedUnits,
                         ClientTurnMessage[] clientTurnMessages, List<TurnCastSpell> turnCastSpells) {
        this.currentAttacks = currentAttacks;
        this.damageUpgradedUnits = damageUpgradedUnits;
        this.rangeUpgradedUnits = rangeUpgradedUnits;
        this.playedUnits = playedUnits;
        this.clientTurnMessages = clientTurnMessages;
        this.turnCastSpells = turnCastSpells;
    }

    public ClientHandler() {
        this.currentAttacks = null;
        this.damageUpgradedUnits = null;
        this.rangeUpgradedUnits = null;
        this.playedUnits = new HashSet<>();
        this.clientTurnMessages = new ClientTurnMessage[4];
        this.turnCastSpells = new ArrayList<>();
    }
}
