package de.janschuri.lunaticlib.platform.bungee.external;

import de.janschuri.lunaticlib.common.futurerequests.requests.HasEnoughMoneyRequest;
import de.janschuri.lunaticlib.common.futurerequests.requests.WithdrawMoneyRequest;
import de.janschuri.lunaticlib.platform.Vault;

import java.util.UUID;

public class VaultImpl implements Vault {
    @Override
    public boolean hasEnoughMoney(String serverName, UUID uuid, double amount) {
        return new HasEnoughMoneyRequest().get(serverName, uuid, amount)
                .thenApply(aBoolean -> aBoolean)
                .join();
    }

    @Override
    public boolean withdrawMoney(String serverName, UUID uuid, double amount) {
        return new WithdrawMoneyRequest().get(serverName, uuid, amount)
                .thenApply(aBoolean -> aBoolean)
                .join();
    }
}
