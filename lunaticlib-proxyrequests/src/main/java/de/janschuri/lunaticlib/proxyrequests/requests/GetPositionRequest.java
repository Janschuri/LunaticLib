package de.janschuri.lunaticlib.proxyrequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class GetPositionRequest extends ProxyRequest<double[]> {

    private static final String REQUEST_NAME = "LunaticLib:GetPosition";
    private static final ConcurrentHashMap<Integer, CompletableFuture<double[]>> REQUEST_MAP = new ConcurrentHashMap<>();

    public GetPositionRequest() {
        super(REQUEST_NAME, REQUEST_MAP);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
        ProxyRequestsPlayerSender player = LunaticProxyRequestsHandler.getAdapter().getPlayerSender(uuid);
        double[] position = player.getPosition();

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
