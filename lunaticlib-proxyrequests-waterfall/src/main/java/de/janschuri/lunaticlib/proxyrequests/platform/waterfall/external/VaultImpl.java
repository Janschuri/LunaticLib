package de.janschuri.lunaticlib.proxyrequests.platform.waterfall.external;

import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.proxyrequests.requests.HasEnoughMoneyRequest;
import de.janschuri.lunaticlib.proxyrequests.requests.WithdrawMoneyRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class VaultImpl implements Vault {
    @Override
    public CompletableFuture<Boolean> hasEnoughMoney(String serverName, UUID uuid, double amount) {
        return new HasEnoughMoneyRequest().get(serverName, uuid, amount);
    }

    @Override
    public CompletableFuture<Boolean> withdrawMoney(String serverName, UUID uuid, double amount) {
        return new WithdrawMoneyRequest().get(serverName, uuid, amount);
    }
}
