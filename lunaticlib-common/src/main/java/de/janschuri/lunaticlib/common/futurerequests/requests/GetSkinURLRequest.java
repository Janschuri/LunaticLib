package de.janschuri.lunaticlib.common.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.common.futurerequests.FutureRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class GetSkinURLRequest extends FutureRequest<String> {

    private static final String REQUEST_NAME = "LunaticLib:GetSkinURL";
    private static final ConcurrentHashMap<Integer, CompletableFuture<String>> REQUEST_MAP = new ConcurrentHashMap<>();

    public GetSkinURLRequest() {
        super(REQUEST_NAME, REQUEST_MAP);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
        PlayerSender player = LunaticLib.getPlatform().getPlayerSender(uuid);
        String skin = player.getSkinURL();

        boolean found = skin != null;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeBoolean(found);
        if (found) {
            out.writeUTF(skin);
        }
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean found = in.readBoolean();
        if (!found) {
            completeRequest(requestId, null);
            return;
        }
        String skin = in.readUTF();
        completeRequest(requestId, skin);
    }

    public CompletableFuture<String> get(UUID uuid) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        return sendRequest(out.toByteArray());
    }
}
