package de.janschuri.lunaticlib.platform;

import java.util.UUID;

public interface Vault {

    boolean hasEnoughMoney(String serverName, UUID uuid, double amount);

    boolean hasEnoughMoney(UUID uuid, double amount);

    boolean withdrawMoney(String serverName, UUID uuid, double amount);

    boolean withdrawMoney(UUID uuid, double amount);
}
