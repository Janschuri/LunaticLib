package de.janschuri.lunaticlib.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.senders.bukkit.PlayerSender;
import de.janschuri.lunaticlib.futurerequests.FutureRequest;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class GetItemInMainHandRequest extends FutureRequest<byte[]> {

    private static final String REQUEST_NAME = "LunaticLib:GetItemInMainHand";
    private static final ConcurrentHashMap<Integer, CompletableFuture<byte[]>> requestMap = new ConcurrentHashMap<>();
    public GetItemInMainHandRequest() {
        super(REQUEST_NAME, requestMap);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());

        if (Bukkit.getPlayer(uuid) == null) {
            return;
        }

        PlayerSender player = new PlayerSender(uuid);

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

    public byte[] get(String serverName, UUID uuid) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        return sendRequest(serverName, out.toByteArray());
    }
}
