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

public class GetItemInMainHandRequest extends FutureRequest<byte[]> {

    private static final String REQUEST_NAME = "LunaticLib:GetItemInMainHand";
    private static final ConcurrentHashMap<Integer, CompletableFuture<byte[]>> REQUEST_MAP = new ConcurrentHashMap<>();

    public GetItemInMainHandRequest() {
        super(REQUEST_NAME, REQUEST_MAP);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());

        PlayerSender player = LunaticLib.getPlatform().getPlayerSender(uuid);

        byte[] item = player.getItemInMainHand();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(item.length);
        out.write(item);
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        int length = in.readInt();
        byte[] item = new byte[length];
        in.readFully(item);
        completeRequest(requestId, item);
    }

    public CompletableFuture<byte[]> get(String serverName, UUID uuid) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        return sendRequest(serverName, out.toByteArray());
    }
}
