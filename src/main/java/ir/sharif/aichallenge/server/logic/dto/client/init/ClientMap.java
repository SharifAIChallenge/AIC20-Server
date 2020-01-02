package ir.sharif.aichallenge.server.logic.dto.client.init;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientMap {
    private int rows;
    private int cols;
    private List<ClientPath> paths;
    private List<ClientBaseKing> kings;
}
