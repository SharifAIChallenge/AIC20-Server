package ir.sharif.aichallenge.server.logic.dto.client.init;

import ir.sharif.aichallenge.server.logic.dto.client.ClientCell;
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
    private ClientCell center;
    private int hp;
    private int attack;
    private int range;
}
