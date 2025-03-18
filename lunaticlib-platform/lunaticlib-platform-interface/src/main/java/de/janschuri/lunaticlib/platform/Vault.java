package de.janschuri.lunaticlib.platform;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Vault {

    CompletableFuture<Boolean> hasEnoughMoney(String serverName, UUID uuid, double amount);

    CompletableFuture<Boolean> withdrawMoney(String serverName, UUID uuid, double amount);
}
