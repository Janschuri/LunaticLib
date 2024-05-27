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

public class RemoveItemInMainHandRequest extends FutureRequest<Boolean> {

    private static final String REQUEST_NAME = "LunaticLib:RemoveItemInMainHand";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> requestMap = new ConcurrentHashMap<>();
    public RemoveItemInMainHandRequest() {
        super(REQUEST_NAME, requestMap);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
         PlayerSender player = LunaticLib.getPlatform().getPlayerSender(uuid);
         boolean removed = player.removeItemInMainHand();

         ByteArrayDataOutput out = ByteStreams.newDataOutput();
         out.writeBoolean(removed);
         sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean removed = in.readBoolean();
        completeRequest(requestId, removed);
    }

    public Boolean get(String serverName, UUID uuid) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        return sendRequest(serverName, out.toByteArray());
    }
}
