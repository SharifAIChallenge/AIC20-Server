package ir.sharif.aichallenge.server.logic.dto.client.end;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerScore {
    private int playerId;
    private int score;
}
