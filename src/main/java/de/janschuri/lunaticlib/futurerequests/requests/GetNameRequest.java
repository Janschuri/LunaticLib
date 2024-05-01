package de.janschuri.lunaticlib.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.VelocityLunaticLib;
import de.janschuri.lunaticlib.senders.paper.PlayerSender;
import de.janschuri.lunaticlib.futurerequests.FutureRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class GetNameRequest extends FutureRequest<String> {

    private static final String REQUEST_NAME = "LunaticLib:GetName";
    private static final ConcurrentHashMap<Integer, CompletableFuture<String>> requestMap = new ConcurrentHashMap<>();

    public GetNameRequest() {
        super(REQUEST_NAME, requestMap);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
        PlayerSender player = new PlayerSender(uuid);
        String name = player.getName();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(name);
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        String name = in.readUTF();
        completeRequest(requestId, name);
    }

    public String get(UUID uuid) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        return sendRequest(out.toByteArray());
    }
}
