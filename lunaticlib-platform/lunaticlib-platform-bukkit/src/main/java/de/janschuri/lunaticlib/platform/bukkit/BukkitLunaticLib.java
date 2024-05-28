package de.janschuri.lunaticlib.platform.bukkit;

import de.janschuri.lunaticlib.platform.Vault;
import de.janschuri.lunaticlib.platform.bukkit.external.AdventureAPI;
import de.janschuri.lunaticlib.platform.bukkit.external.LogBlock;
import de.janschuri.lunaticlib.platform.bukkit.external.Metrics;
import de.janschuri.lunaticlib.platform.bukkit.external.VaultImpl;
import de.janschuri.lunaticlib.platform.bukkit.listener.MessageListener;
import de.janschuri.lunaticlib.platform.Platform;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.common.utils.Mode;
import de.janschuri.lunaticlib.common.utils.Utils;
import de.janschuri.lunaticlib.nms.VersionEnum;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

import static de.janschuri.lunaticlib.common.LunaticLib.IDENTIFIER;

public class BukkitLunaticLib extends JavaPlugin {
    private static BukkitLunaticLib instance;
    private static boolean installedVault = false;
    private static boolean installedLogBlock = false;
    private static VersionEnum version = null;
    private static Vault vault;
    private static LogBlock logBlock;

    @Override
    public void onEnable() {
        instance = this;
        version = getServerVersion();

        AdventureAPI.initialize(this);

        getServer().getMessenger().registerIncomingPluginChannel(this, IDENTIFIER, new MessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, IDENTIFIER);


        if (Utils.classExists("net.milkbowl.vault.economy.Economy")) {
            BukkitLunaticLib.installedVault = true;
            vault = new VaultImpl();
        }

        if (Utils.classExists("de.diddiz.LogBlock.LogBlock")) {
            BukkitLunaticLib.installedLogBlock = true;
        }

        int pluginId = 21913;
        Metrics metrics = new Metrics(this, pluginId);

        Path dataDirectory = getDataFolder().toPath();
        Mode mode = Mode.BACKEND;
        Platform platform = new PlatformImpl();

        LunaticLib.onEnable(dataDirectory, mode, platform);
    }

    public static BukkitLunaticLib getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        AdventureAPI.close();
        LunaticLib.onDisable();
    }

    public static boolean sendPluginMessage(byte[] message) {
        getInstance().getServer().sendPluginMessage(getInstance(), IDENTIFIER, message);
        return true;
    }

    private static void disable() {
        Logger.errorLog("Disabling LunaticLib...");
        Bukkit.getServer().getPluginManager().disablePlugin(getInstance());
    }

    public static void sendConsoleCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    }

    public static VersionEnum getServerVersion() {
        if (version != null) {
            return version;
        }

        String packageName = Bukkit.getServer().getClass().getPackage().getName();

        String versionString = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            return VersionEnum.valueOf(versionString);
        } catch (IllegalArgumentException e) {
            Logger.errorLog("Unknown server version: " + versionString);
            return VersionEnum.UNKNOWN;
        }
    }

    public static boolean isInstalledVault() {
        return installedVault;
    }

    public static boolean isInstalledLogBlock() {
        return installedLogBlock;
    }

    public static Vault getVault() {
        if (!installedVault) {
            Logger.errorLog("Vault is not installed.");
            return null;
        }
        return vault;
    }
}
