package de.janschuri.lunaticlib.external;

import de.janschuri.lunaticlib.LunaticLib;
import de.janschuri.lunaticlib.futurerequests.requests.HasEnoughMoneyRequest;
import de.janschuri.lunaticlib.futurerequests.requests.WithdrawMoneyRequest;
import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.logger.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class Vault {

    private static Economy econ = null;
    public Vault() {
        if (!LunaticLib.isInstalledVault()) {
            Logger.warnLog("Vault is not installed! Please install Vault or disable it in plugin config.yml.");
        }

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

    public static boolean hasEnoughMoney(String serverName, UUID uuid, double amount) {
        if (LunaticLib.getMode() == Mode.PROXY) {
            return new HasEnoughMoneyRequest().get(serverName, uuid, amount);
        }
        return econ.has(Bukkit.getOfflinePlayer(uuid), amount);
    }

    public static boolean hasEnoughMoney(UUID uuid, double amount) {
        if(LunaticLib.getMode() == Mode.PROXY) {
            Logger.errorLog("You can't use this method in PROXY mode.");
        }
        return econ.has(Bukkit.getOfflinePlayer(uuid), amount);
    }

    public static boolean withdrawMoney(String serverName, UUID uuid, double amount) {
        if (LunaticLib.getMode() == Mode.PROXY) {
            return new WithdrawMoneyRequest().get(serverName, uuid, amount);
        }
        return econ.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount).transactionSuccess();
    }

    public static boolean withdrawMoney(UUID uuid, double amount) {
        if(LunaticLib.getMode() == Mode.PROXY) {
            Logger.errorLog("You can't use this method in PROXY mode.");
        }
        return econ.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount).transactionSuccess();
    }
}
