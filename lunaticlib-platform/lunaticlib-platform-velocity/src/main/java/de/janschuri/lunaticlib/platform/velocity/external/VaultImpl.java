package de.janschuri.lunaticlib.platform.velocity.external;

import de.janschuri.lunaticlib.common.futurerequests.requests.HasEnoughMoneyRequest;
import de.janschuri.lunaticlib.common.futurerequests.requests.WithdrawMoneyRequest;
import de.janschuri.lunaticlib.platform.Vault;

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
