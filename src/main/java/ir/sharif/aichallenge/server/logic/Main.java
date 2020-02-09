package ir.sharif.aichallenge.server.logic;

import ir.sharif.aichallenge.server.engine.core.GameServer;
import ir.sharif.aichallenge.server.utils.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        AtomicInteger currentTurn = new AtomicInteger(0);
        Log.outputFile = new PrintStream(new FileOutputStream("server.log", false));
        int extraTime = extractExtraTime(args);

        GameServer gameServer = new GameServer(new GameHandler(currentTurn, extraTime), args, currentTurn);
        gameServer.start();
        gameServer.waitForFinish();
    }

    private static int extractExtraTime(String[] args) {
        int extraTime = 0;
        try {
            for (String arg : args) {
                if (!arg.startsWith("--extra=") && !arg.startsWith("--extra:")) {
                    continue;
                }
                extraTime = Integer.parseInt(arg.substring(8));
                return extraTime;
            }
        } catch (Exception e) {
            return extraTime;
        }

        return extraTime;
    }
}
