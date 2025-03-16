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

public class IsInRangeRequest extends FutureRequest<Boolean> {

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

        PlayerSender player = LunaticLib.getPlatform().getPlayerSender(uuid1);
        PlayerSender partner = LunaticLib.getPlatform().getPlayerSender(uuid2);
        if (!player.isOnline() || !partner.isOnline()) {
            return;
        }

        boolean isInRange = player.isInRange(uuid2, range)
                .thenApply(isInRangeResult -> isInRangeResult)
                .join();
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
