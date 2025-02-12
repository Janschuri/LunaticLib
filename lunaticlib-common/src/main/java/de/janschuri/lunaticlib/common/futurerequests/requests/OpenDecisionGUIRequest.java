package de.janschuri.lunaticlib.common.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.DecisionMessage;
import de.janschuri.lunaticlib.common.command.LunaticDecisionMessage;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.futurerequests.FutureRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class OpenDecisionGUIRequest extends FutureRequest<Boolean> {
    private static final String REQUEST_NAME = "OpenDecisionGUI";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> requestMap = new ConcurrentHashMap<>();
    public OpenDecisionGUIRequest() {
        super(REQUEST_NAME, requestMap);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
        PlayerSender player = LunaticLib.getPlatform().getPlayerSender(uuid);

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

    public Boolean get(String serverName, UUID uuid, DecisionMessage decisionMessage) {
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