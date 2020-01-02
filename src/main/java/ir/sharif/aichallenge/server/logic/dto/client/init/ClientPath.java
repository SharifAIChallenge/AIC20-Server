package ir.sharif.aichallenge.server.logic.dto.client.init;

import ir.sharif.aichallenge.server.logic.dto.client.ClientCell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientPath {
    private int id;
    private List<ClientCell> cells;
}
