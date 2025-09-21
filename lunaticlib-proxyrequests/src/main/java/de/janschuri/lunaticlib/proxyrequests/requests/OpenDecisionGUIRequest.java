package de.janschuri.lunaticlib.proxyrequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.utils.DecisionMessage;
import de.janschuri.lunaticlib.utils.LunaticDecisionMessage;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class OpenDecisionGUIRequest extends ProxyRequest<Boolean> {
    private static final String REQUEST_NAME = "OpenDecisionGUI";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> REQUEST_MAP = new ConcurrentHashMap<>();

    public OpenDecisionGUIRequest() {
        super(REQUEST_NAME, REQUEST_MAP);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
        ProxyRequestsPlayerSender player = LunaticProxyRequestsHandler.getAdapter().getPlayerSender(uuid);

        int length = in.readInt();
        String[] message = new String[length];
        for (int i = 0; i < length; i++) {
            message[i] = in.readUTF();
        }

        LunaticDecisionMessage decisionMessage = LunaticDecisionMessage.fromStringArray(message);
        decisionMessage.setExecuteFromBackend(true);
        boolean success = player.openDecisionGUI(decisionMessage);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeBoolean(success);
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean success = in.readBoolean();
        completeRequest(requestId, success);
    }

    public CompletableFuture<Boolean> get(String serverName, UUID uuid, DecisionMessage decisionMessage) {
        String[] message = decisionMessage.toStringArray();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());

        out.writeInt(message.length);
        for (String s : message) {
            out.writeUTF(s);
        }

        return sendRequest(serverName, out.toByteArray());
    }
}