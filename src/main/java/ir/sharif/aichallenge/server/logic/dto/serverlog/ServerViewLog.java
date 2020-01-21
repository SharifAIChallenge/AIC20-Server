package ir.sharif.aichallenge.server.logic.dto.serverlog;

import ir.sharif.aichallenge.server.logic.dto.client.init.InitialMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerViewLog {
    private InitialMessage init;
    private List<TurnInfo> turns;
}
