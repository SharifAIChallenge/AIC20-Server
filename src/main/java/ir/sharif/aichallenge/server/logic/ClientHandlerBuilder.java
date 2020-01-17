package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.logic.dto.client.turn.ClientTurnMessage;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnCastSpell;
import ir.sharif.aichallenge.server.logic.dto.graphic.turn.TurnAttack;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ClientHandlerBuilder {
    private List<TurnAttack> currentAttacks;
    private Set<Integer> damageUpgradedUnits;
    private Set<Integer> rangeUpgradedUnits;
    private Set<Integer> playedUnits;
    private ClientTurnMessage[] clientTurnMessages;
    private List<TurnCastSpell> turnCastSpells;

    public ClientHandlerBuilder(ClientHandler clientHandler) {
        this.currentAttacks = clientHandler.getCurrentAttacks();
        this.damageUpgradedUnits = clientHandler.getDamageUpgradedUnits();
        this.rangeUpgradedUnits = clientHandler.getRangeUpgradedUnits();
        this.playedUnits = clientHandler.getPlayedUnits();
        this.clientTurnMessages = clientHandler.getClientTurnMessages();
        this.turnCastSpells = clientHandler.getTurnCastSpells();
    }

    public ClientHandlerBuilder() {
        this(new ClientHandler());
    }

    public ClientHandler toClientHandler() {
        return new ClientHandler(currentAttacks, damageUpgradedUnits, rangeUpgradedUnits,
                playedUnits, clientTurnMessages, turnCastSpells);
    }
}
