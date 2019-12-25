package ir.sharif.aichallenge.server.logic.dto.init;

import ir.sharif.aichallenge.server.logic.dto.ClientCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientBaseKing {
    private int playerId;
    private boolean isYou;
    private boolean isYourFriend;
    private ClientCell center;
    private int hp;
    private int attack;
    private int range;
}
