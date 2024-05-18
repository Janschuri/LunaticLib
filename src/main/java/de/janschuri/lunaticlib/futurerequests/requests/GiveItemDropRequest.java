package de.janschuri.lunaticlib.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.senders.bukkit.PlayerSender;
import de.janschuri.lunaticlib.futurerequests.FutureRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class GiveItemDropRequest extends FutureRequest<Boolean> {
    private static final String REQUEST_NAME = "LunaticLib:GiveItemDrop";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> requestMap = new ConcurrentHashMap<>();

    public GiveItemDropRequest() {
        super(REQUEST_NAME, requestMap);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
        int length = in.readInt();
        byte[] item = new byte[length];
        in.readFully(item);

        PlayerSender player = new PlayerSender(uuid);

        boolean dropped = player.giveItemDrop(item);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeBoolean(dropped);
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean dropped = in.readBoolean();
        completeRequest(requestId, dropped);
    }

    public boolean get(String serverName, UUID uuid, byte[] item) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        out.writeInt(item.length);
        out.write(item);
        return sendRequest(serverName, out.toByteArray());
    }
}
