package de.janschuri.lunaticlib.platform.bukkit.external;

import de.janschuri.lunaticlib.platform.Vault;
import de.janschuri.lunaticlib.common.logger.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

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

    public boolean hasEnoughMoney(String serverName, UUID uuid, double amount) {
        return econ.has(Bukkit.getOfflinePlayer(uuid), amount);
    }

    public boolean hasEnoughMoney(UUID uuid, double amount) {
        return econ.has(Bukkit.getOfflinePlayer(uuid), amount);
    }

    public boolean withdrawMoney(String serverName, UUID uuid, double amount) {
        return econ.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount).transactionSuccess();
    }

    public boolean withdrawMoney(UUID uuid, double amount) {
        return econ.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount).transactionSuccess();
    }
}
