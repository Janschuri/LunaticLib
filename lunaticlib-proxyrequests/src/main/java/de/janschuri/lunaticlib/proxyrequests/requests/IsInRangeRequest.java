package de.janschuri.lunaticlib.proxyrequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class IsInRangeRequest extends ProxyRequest<Boolean> {

    private static final String REQUEST_NAME = "LunaticLib:IsInRange";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> REQUEST_MAP = new ConcurrentHashMap<>();

    public IsInRangeRequest() {
        super(REQUEST_NAME, REQUEST_MAP);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid1 = UUID.fromString(in.readUTF());
        UUID uuid2 = UUID.fromString(in.readUTF());
        double range = in.readDouble();

        ProxyRequestsPlayerSender player = LunaticProxyRequestsHandler.getAdapter().getPlayerSender(uuid1);
        ProxyRequestsPlayerSender partner = LunaticProxyRequestsHandler.getAdapter().getPlayerSender(uuid2);
        if (player == null  || partner == null) {
            return;
        }

        boolean isInRange = player.isInRange(partner, range);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeBoolean(isInRange);
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean isInRange = in.readBoolean();
        completeRequest(requestId, isInRange);
    }

    public CompletableFuture<Boolean> get(String serverName, UUID uuid1, UUID uuid2, double range) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid1.toString());
        out.writeUTF(uuid2.toString());
        out.writeDouble(range);
        return sendRequest(serverName, out.toByteArray());
    }
}
