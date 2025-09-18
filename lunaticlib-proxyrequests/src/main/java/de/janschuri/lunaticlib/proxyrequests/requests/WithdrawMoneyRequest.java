package de.janschuri.lunaticlib.proxyrequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsLogger;
import de.janschuri.lunaticlib.proxyrequests.external.Vault;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class WithdrawMoneyRequest extends ProxyRequest<Boolean> {

    private static final String REQUEST_NAME = "LunaticLib:WithdrawMoney";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> REQUEST_MAP = new ConcurrentHashMap<>();

    public WithdrawMoneyRequest() {
        super(REQUEST_NAME, REQUEST_MAP);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {

        UUID uuid = UUID.fromString(in.readUTF());
        double amount = in.readDouble();

        Vault vault = LunaticProxyRequestsHandler.adapter().getVault();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        if (vault != null) {
            vault.withdrawMoney("", uuid, amount)
                    .thenAccept(success -> {
                        out.writeBoolean(true);
                        out.writeBoolean(success);
                        sendResponse(requestId, out.toByteArray());
                    });

        } else {
            out.writeBoolean(false);
            out.writeBoolean(false);
            sendResponse(requestId, out.toByteArray());
        }
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean vaultAvailable = in.readBoolean();

        if (!vaultAvailable) {
            ProxyRequestsLogger.error("Vault not available. Disable or install it.");
            completeRequest(requestId, false);
            return;
        }

        boolean success = in.readBoolean();
        completeRequest(requestId, success);
    }

    public CompletableFuture<Boolean> get(String serverName, UUID uuid, double amount) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        out.writeDouble(amount);
        return sendRequest(serverName, out.toByteArray());
    }
}
