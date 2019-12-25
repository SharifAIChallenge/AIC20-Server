package ir.sharif.aichallenge.server.logic.dto.turn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnKing {
    private int playerId;
    private boolean isAlive;
    private int hp;
}
