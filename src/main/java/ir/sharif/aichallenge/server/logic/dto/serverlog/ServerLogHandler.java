package ir.sharif.aichallenge.server.logic.dto.serverlog;

import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.dto.client.init.InitialMessage;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnUnit;
import ir.sharif.aichallenge.server.logic.entities.Player;

import java.util.ArrayList;
import java.util.List;

public class ServerLogHandler {
    public static InitialMessage makeInitMessage(InitialMessage initialMessage) {
        InitialMessage init = new InitialMessage();
        init.setMap(initialMessage.getMap());
        init.setSpells(initialMessage.getSpells());
        init.setBaseUnits(initialMessage.getBaseUnits());
        init.setGameConstants(initialMessage.getGameConstants());
        return init;
    }

    public static TurnInfo getTurnInfo(Game game) {
        TurnInfo turnInfo = new TurnInfo();
        List<PlayerInfo> playerInfoList = new ArrayList<>();

        for (int pId = 0; pId<4; pId ++) {
            PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(game.getPlayers()[pId], game);
            playerInfoList.add(playerInfo);
        }

        turnInfo.setPlayers(playerInfoList);
        turnInfo.setTurnNum(game.getCurrentTurn().get());
        turnInfo.setAttacks(game.getCurrentAttacks());

        turnInfo.setKings(game.getClientTurnMessages()[0].getKings());
        turnInfo.setCastSpells(game.getClientTurnMessages()[0].getCastSpells());

        turnInfo.setUnits(game.getClientTurnMessages()[0].getUnits());

        //todo
        turnInfo.setPutUnits(game.getCurrentPutUnits());

        List<TurnUnit> diedUnits = new ArrayList<>();
        for (int id : game.getDiedUnits()) {
            for (TurnUnit turnUnit : turnInfo.getUnits())
                if(turnUnit.getUnitId() == id)
                    diedUnits.add(turnUnit);
        }
        turnInfo.setDiedUnits(diedUnits);

        turnInfo.setUpgradedUnits(game.getCurrentUpgradedUnits());

        return turnInfo;
    }

    public static void saveServerLog(ServerViewLog serverViewLog) {
    }
}
