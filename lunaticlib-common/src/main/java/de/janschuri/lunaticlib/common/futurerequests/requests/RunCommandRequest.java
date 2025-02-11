package de.janschuri.lunaticlib.common.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sun.jdi.VoidType;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.futurerequests.FutureRequest;
import de.janschuri.lunaticlib.common.logger.Logger;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RunCommandRequest extends FutureRequest<Boolean> {

    private static final String REQUEST_NAME = "LunaticLib:RunCommand";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> requestMap = new ConcurrentHashMap<>();

    public RunCommandRequest() {
        super(REQUEST_NAME, requestMap);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        UUID uuid = UUID.fromString(in.readUTF());
        String command = in.readUTF();

        PlayerSender player = LunaticLib.getPlatform().getPlayerSender(uuid);

        boolean found = player != null && player.isOnline();

        if (found) {
            boolean success = false;

            try {
                player.runCommand(command);
                success = true;
            } catch (Exception e) {
                Logger.errorLog("RunCommandRequest: Error while running command.");
            }
        }
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean success = in.readBoolean();
        completeRequest(requestId, success);
    }

    public CompletableFuture<Boolean> getAsync(UUID uuid, String command) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        out.writeUTF(command);

        // Send the request asynchronously
        return sendRequestAsync(out.toByteArray()).thenApply(response -> {
            if (response == null || !(response instanceof Boolean)) {
                Logger.errorLog("RunCommandRequest: Error while running command.");
                return false; // Indicate failure if the response is invalid
            }
            return response; // Return the actual success/failure response from Velocity
        }).exceptionally(ex -> {
            Logger.errorLog("RunCommandRequest: Exception occurred - " + ex.getMessage());
            return false; // Indicate failure on exception
        });
    }
}
