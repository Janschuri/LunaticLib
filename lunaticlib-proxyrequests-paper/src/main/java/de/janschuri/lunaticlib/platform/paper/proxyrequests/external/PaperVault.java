package de.janschuri.lunaticlib.platform.paper.proxyrequests.external;

import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsLogger;
import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PaperVault implements Vault {

    private static Economy econ = null;
    public PaperVault() {
        if (!setupEconomy()) {
            ProxyRequestsLogger.warn("Could not setup Economy.");
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
