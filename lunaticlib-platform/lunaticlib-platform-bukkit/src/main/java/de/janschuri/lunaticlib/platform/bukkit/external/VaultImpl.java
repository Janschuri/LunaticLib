package de.janschuri.lunaticlib.platform.bukkit.external;

import de.janschuri.lunaticlib.platform.Vault;
import de.janschuri.lunaticlib.common.logger.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.units.qual.C;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class VaultImpl implements Vault {

    private static Economy econ = null;
    public VaultImpl() {
        if (!setupEconomy() ) {
            Logger.warnLog("Could not setup Economy.");
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public CompletableFuture<Boolean> hasEnoughMoney(String serverName, UUID uuid, double amount) {
        return CompletableFuture.completedFuture(econ.has(Bukkit.getOfflinePlayer(uuid), amount));
    }

    public CompletableFuture<Boolean> withdrawMoney(String serverName, UUID uuid, double amount) {
        return CompletableFuture.completedFuture(econ.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount).transactionSuccess());
    }
}
