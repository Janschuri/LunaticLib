package de.janschuri.lunaticlib.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.senders.paper.PlayerSender;
import de.janschuri.lunaticlib.futurerequests.FutureRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class IsInRangeRequest extends FutureRequest<Boolean> {

    private static final String REQUEST_NAME = "LunaticLib:IsInRange";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> requestMap = new ConcurrentHashMap<>();


    public IsInRangeRequest() {
        super(REQUEST_NAME, requestMap);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid1 = UUID.fromString(in.readUTF());
        UUID uuid2 = UUID.fromString(in.readUTF());
        double range = in.readDouble();

        PlayerSender player = new PlayerSender(uuid1);
        PlayerSender partner = new PlayerSender(uuid2);
        if (!player.isOnline() || !partner.isOnline()) {
            return;
        }

        boolean isInRange = player.isInRange(uuid2, range);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeBoolean(isInRange);
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean isInRange = in.readBoolean();
        completeRequest(requestId, isInRange);
    }

    public Boolean get(String serverName, UUID uuid1, UUID uuid2, double range) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid1.toString());
        out.writeUTF(uuid2.toString());
        out.writeDouble(range);
        return sendRequest(serverName, out.toByteArray());
    }
}
