package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.engine.core.GameServer;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger currentTurn = new AtomicInteger(0);

        GameServer gameServer = new GameServer(new GameHandler(currentTurn), args, currentTurn);
        gameServer.start();
        gameServer.waitForFinish();
    }
}
