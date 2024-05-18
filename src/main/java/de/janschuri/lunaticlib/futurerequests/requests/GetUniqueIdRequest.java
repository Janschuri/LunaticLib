package de.janschuri.lunaticlib.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.senders.bukkit.PlayerSender;
import de.janschuri.lunaticlib.futurerequests.FutureRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class GetUniqueIdRequest extends FutureRequest<UUID> {

    private static final String REQUEST_NAME = "LunaticLib:GetUniqueId";
    private static final ConcurrentHashMap<Integer, CompletableFuture<UUID>> requestMap = new ConcurrentHashMap<>();

    public GetUniqueIdRequest() {
        super(REQUEST_NAME, requestMap);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        String name = in.readUTF();
        PlayerSender player = new PlayerSender(name);
        UUID uuid = player.getUniqueId();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
        completeRequest(requestId, uuid);
    }

    public UUID get(String name) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(name);
        return sendRequest(out.toByteArray());
    }
}
