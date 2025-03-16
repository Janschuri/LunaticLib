package de.janschuri.lunaticlib.common.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.futurerequests.FutureRequest;
import de.janschuri.lunaticlib.common.logger.Logger;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RunCommandRequest extends FutureRequest<Boolean> {

    private static final String REQUEST_NAME = "LunaticLib:RunCommand";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> REQUEST_MAP = new ConcurrentHashMap<>();

    public RunCommandRequest() {
        super(REQUEST_NAME, REQUEST_MAP);
        this.suppressTimeoutException();
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
        String command = in.readUTF();

        PlayerSender player = LunaticLib.getPlatform().getPlayerSender(uuid);

        boolean found = player != null && player.isOnline();
        boolean success = false;

        if (found) {
            try {
                player.runCommand(command);
                success = true;
            } catch (Exception e) {
                success = false;
                Logger.errorLog("RunCommandRequest: Error while running command.");
            }
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeBoolean(success);

        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean success = in.readBoolean();
        completeRequest(requestId, success);
    }

    public CompletableFuture<Boolean> get(UUID uuid, String command) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        out.writeUTF(command);

        return sendRequest(out.toByteArray());
    }
}
