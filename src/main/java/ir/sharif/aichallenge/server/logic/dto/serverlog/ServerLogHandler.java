package ir.sharif.aichallenge.server.logic.dto.serverlog;

import com.google.gson.Gson;
import ir.sharif.aichallenge.server.logic.Game;
import ir.sharif.aichallenge.server.logic.dto.client.init.InitialMessage;
import ir.sharif.aichallenge.server.logic.dto.client.turn.TurnUnit;
import ir.sharif.aichallenge.server.logic.entities.units.KingUnit;
import ir.sharif.aichallenge.server.logic.entities.units.Unit;

import java.io.IOException;
import java.io.RandomAccessFile;
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

        List<TurnUnit> units = new ArrayList<>();
        for (Unit unit : game.getUnitsWithId().values())
            if(!(unit instanceof KingUnit))
                units.add(game.buildTurnUnit(unit));

        turnInfo.setUnits(units);

        turnInfo.setPutUnits(game.getCurrentPutUnits());

        List<TurnUnit> diedUnits = new ArrayList<>();
        for (Unit deadUnit : game.getDeadUnits())
            diedUnits.add(game.buildTurnUnit(deadUnit));

        turnInfo.setDiedUnits(diedUnits);

        turnInfo.setDamageUpgradedUnits(new ArrayList<Integer>(game.getDamageUpgradedUnits()));
        turnInfo.setRangeUpgradedUnits(new ArrayList<Integer>(game.getRangeUpgradedUnits()));

        return turnInfo;
    }

    public static void saveServerLog(ServerViewLog serverViewLog) {
        RandomAccessFile file;
        String serverLog = new Gson().toJson(serverViewLog);
        try {
            file = new RandomAccessFile("server_log.json", "rwd");
            file.setLength(0);
            file.write(serverLog.getBytes());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
