package ir.sharif.aichallenge.server.logic.dto.init;

import ir.sharif.aichallenge.server.logic.dto.ClientCell;
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
