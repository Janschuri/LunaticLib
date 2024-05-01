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

public class WithdrawMoneyRequest extends FutureRequest<Boolean> {

    private static final String REQUEST_NAME = "LunaticLib:WithdrawMoney";
    private static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> REQUEST_MAP = new ConcurrentHashMap<>();

    public WithdrawMoneyRequest() {
        super(REQUEST_NAME, REQUEST_MAP);
    }

    @Override
    protected void handleRequest(int requestId, ByteArrayDataInput in) {
        boolean success = false;
        if (!LunaticLib.installedVault) {
            success = false;
        } else {
            UUID uuid = UUID.fromString(in.readUTF());
            double amount = in.readDouble();
            success = Vault.withdrawMoney(uuid, amount);
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

    public boolean get(String serverName, UUID uuid, double amount) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        out.writeDouble(amount);
        return sendRequest(serverName, out.toByteArray());
    }
}
