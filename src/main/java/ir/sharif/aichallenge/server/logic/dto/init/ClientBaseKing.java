package ir.sharif.aichallenge.server.logic.dto.init;

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
    private int row;
    private int col;
    private int hp;
    private int attack;
    private int range;
}
