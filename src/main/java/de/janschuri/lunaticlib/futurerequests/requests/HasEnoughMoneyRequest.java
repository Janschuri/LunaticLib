package de.janschuri.lunaticlib.futurerequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.LunaticLib;
import de.janschuri.lunaticlib.external.Vault;
import de.janschuri.lunaticlib.futurerequests.FutureRequest;

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
        boolean hasEnoughMoney = false;
        if (!LunaticLib.installedVault) {
            hasEnoughMoney = false;
        } else {
            UUID uuid = UUID.fromString(in.readUTF());
            double amount = in.readDouble();
            hasEnoughMoney = Vault.hasEnoughMoney(uuid, amount);
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeBoolean(hasEnoughMoney);
        sendResponse(requestId, out.toByteArray());
    }

    @Override
    protected void handleResponse(int requestId, ByteArrayDataInput in) {
        boolean hasEnoughMoney = in.readBoolean();
        completeRequest(requestId, hasEnoughMoney);
    }

    public boolean get(String serverName, UUID uuid, double amount) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        out.writeDouble(amount);
        return sendRequest(serverName, out.toByteArray());
    }
}
