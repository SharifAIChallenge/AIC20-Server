package ir.sharif.aichallenge.server.logic.dto.client.end;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndMessage {
    private int winnerTeam;     //0, 1, -1 if draw
    private List<PlayerEndInfo> players;
}
