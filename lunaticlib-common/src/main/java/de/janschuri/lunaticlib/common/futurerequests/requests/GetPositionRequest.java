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

public class GetPositionRequest extends FutureRequest<double[]> {

    private static final String REQUEST_NAME = "LunaticLib:GetPosition";
    private static final ConcurrentHashMap<Integer, CompletableFuture<double[]>> REQUEST_MAP = new ConcurrentHashMap<>();

    public GetPositionRequest() {
        super(REQUEST_NAME, REQUEST_MAP);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
        PlayerSender player = LunaticLib.getPlatform().getPlayerSender(uuid);
        double[] position = player.getPosition()
                .thenApply(pos -> pos)
                .join();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeDouble(position[0]);
        out.writeDouble(position[1]);
        out.writeDouble(position[2]);
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        double[] position = new double[3];
        position[0] = in.readDouble();
        position[1] = in.readDouble();
        position[2] = in.readDouble();
        completeRequest(requestId, position);
    }

    public CompletableFuture<double[]> get(String serverName, UUID uuid) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        return sendRequest(serverName, out.toByteArray());
    }
}
