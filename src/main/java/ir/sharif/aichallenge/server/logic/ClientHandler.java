package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.logic.dto.client.turn.ClientTurnMessage;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnCastSpell;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.TurnAttack;
import lombok.Getter;

import java.util.*;

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

    public List<TurnAttack> getCurrentAttacks() {
        return currentAttacks == null ? null : new ArrayList<>(currentAttacks);
    }

    public Set<Integer> getDamageUpgradedUnits() {
        return damageUpgradedUnits == null ? null : new HashSet<>(damageUpgradedUnits);
    }

    public Set<Integer> getRangeUpgradedUnits() {
        return rangeUpgradedUnits == null ? null : new HashSet<>(rangeUpgradedUnits);
    }

    public Set<Integer> getPlayedUnits() {
        return playedUnits == null ? null : new HashSet<>(playedUnits);
    }

    public ClientTurnMessage[] getClientTurnMessages() {
        return clientTurnMessages == null ? null : Arrays.copyOf(clientTurnMessages, clientTurnMessages.length);
    }

    public List<TurnCastSpell> getTurnCastSpells() {
        return turnCastSpells == null ? null : new ArrayList<>(turnCastSpells);
    }
}
