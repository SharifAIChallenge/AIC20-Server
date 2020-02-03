package ir.sharif.aichallenge.server.logic.dto.serverlog;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.entities.Player;
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
    private int rangeUpgradedUnit;
    private int damageUpgradedUnit;
    private int remainingAP;

    public static PlayerInfo getPlayerInfo(Player player, Game game) {
        int pId = player.getId(), friendId = pId ^ 2;

        PlayerInfo playerInfo = new PlayerInfo();

        playerInfo.setDeck(player.getDeckIds());
        playerInfo.setHand(player.getHandIds());

        playerInfo.setAvailableDamageUpgrades(player.getNumberOfDamageUpgrades());
        playerInfo.setAvailableRangeUpgrades(player.getNumberOfRangeUpgrades());

        Player friend = game.getPlayers()[friendId];
        playerInfo.setFriendSpells(friend.getAvailableSpellIds());
        playerInfo.setMySpells(player.getAvailableSpellIds());

        playerInfo.setGotDamageUpgrade(player.isGotDamageUpgrade());
        playerInfo.setGotRangeUpgrade(player.isGotRangeUpgrade());
        playerInfo.setReceivedSpell(player.getReceivedSpell());

        playerInfo.setRemainingAP(player.getAp());

        playerInfo.setFriendReceivedSpell(game.getPlayers()[friendId].getReceivedSpell());

        return playerInfo;
    }

}
