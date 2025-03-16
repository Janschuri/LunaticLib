package de.janschuri.lunaticlib.common.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.futurerequests.FutureRequest;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.Vault;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class HasEnoughMoneyRequest extends FutureRequest<Boolean> {

    private static final String REQUEST_NAME = "LunaticLib:HasEnoughMoney";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> REQUEST_MAP = new ConcurrentHashMap<>();

    public HasEnoughMoneyRequest() {
        super(REQUEST_NAME, REQUEST_MAP);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        boolean vaultAvailable = false;
        boolean hasEnoughMoney = false;

        UUID uuid = UUID.fromString(in.readUTF());
        double amount = in.readDouble();
        Vault vault = LunaticLib.getPlatform().getVault();
        if (vault != null) {
            vaultAvailable = true;
            hasEnoughMoney = vault.hasEnoughMoney("", uuid, amount);
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeBoolean(vaultAvailable);
        out.writeBoolean(hasEnoughMoney);
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean vaultAvailable = in.readBoolean();

        if (!vaultAvailable) {
            Logger.errorLog("Vault not available. Disable or install it.");
            completeRequest(requestId, false);
            return;
        }

        boolean hasEnoughMoney = in.readBoolean();
        completeRequest(requestId, hasEnoughMoney);
    }

    public CompletableFuture<Boolean> get(String serverName, UUID uuid, double amount) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        out.writeDouble(amount);
        return sendRequest(serverName, out.toByteArray());
    }
}
