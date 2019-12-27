package ir.sharif.aichallenge.server.common.network.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ir.sharif.aichallenge.server.common.network.Json;
import lombok.Getter;

@Getter
public class ClientMessage extends Message {

    private final int turn;

    public ClientMessage(String type, JsonObject info, int turn) {
        super(type, info);
        this.turn = turn;
    }

    public ClientMessageInfo getParsedInfo() {
        return Json.GSON.fromJson(this.getInfo(), ClientMessageInfo.class);
    }
}
