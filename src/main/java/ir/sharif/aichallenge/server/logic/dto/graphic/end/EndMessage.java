package ir.sharif.aichallenge.server.logic.dto.graphic.end;

import ir.sharif.aichallenge.server.logic.dto.client.end.PlayerScore;
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
    private List<PlayerScore> scores;
}
