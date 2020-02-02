package ir.sharif.aichallenge.server.logic.dto.client.end;

import ir.sharif.aichallenge.server.logic.dto.client.turn.ClientTurnMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientEndMessage {
    private ClientTurnMessage turnMessage;
    private EndMessage end;
}
