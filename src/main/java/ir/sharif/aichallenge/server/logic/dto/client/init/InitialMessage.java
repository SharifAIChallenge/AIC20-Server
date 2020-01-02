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
public class InitialMessage {       //todo set primitives to classes if necessary (because null values be null)
    private GameConstants gameConstants;
    private ClientMap map;
    private List<ClientBaseUnit> baseUnits;
    private List<ClientSpell> spells;
}
