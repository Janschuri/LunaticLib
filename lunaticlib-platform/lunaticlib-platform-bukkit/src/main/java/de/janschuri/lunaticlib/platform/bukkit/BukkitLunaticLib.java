package de.janschuri.lunaticlib.platform.bukkit;

import de.janschuri.lunaticlib.nms.Version;
import de.janschuri.lunaticlib.platform.Vault;
import de.janschuri.lunaticlib.platform.bukkit.external.AdventureAPI;
import de.janschuri.lunaticlib.platform.bukkit.external.LogBlock;
import de.janschuri.lunaticlib.platform.bukkit.external.Metrics;
import de.janschuri.lunaticlib.platform.bukkit.external.VaultImpl;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIListener;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
import de.janschuri.lunaticlib.platform.bukkit.listener.MessageListener;
import de.janschuri.lunaticlib.platform.Platform;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.common.utils.Mode;
import de.janschuri.lunaticlib.common.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

import static de.janschuri.lunaticlib.common.LunaticLib.IDENTIFIER;

public class BukkitLunaticLib extends JavaPlugin {
    private static BukkitLunaticLib instance;
    private static boolean installedVault = false;
    private static boolean installedLogBlock = false;
    private static Version version;
    private static Vault vault;

    private GUIManager guiManager;

    @Override
    public void onEnable() {
        instance = this;
        version = getServerVersion();
        guiManager = new GUIManager();

        if (version == null) {
            disable();
            return;
        }

        AdventureAPI.initialize(this);

        getServer().getMessenger().registerIncomingPluginChannel(this, IDENTIFIER, new MessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, IDENTIFIER);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);

        int pluginId = 21913;
        Metrics metrics = new Metrics(this, pluginId);

        Path dataDirectory = getDataFolder().toPath();
        Mode mode = Mode.BACKEND;
        Platform platform = new PlatformImpl();

        LunaticLib.onEnable(dataDirectory, mode, platform);

        if (Utils.classExists("net.milkbowl.vault.economy.Economy")) {
            installedVault = true;
            Logger.debugLog("Vault is installed.");
            vault = new VaultImpl();
        }

        if (Utils.classExists("de.diddiz.LogBlock.LogBlock")) {
            installedLogBlock = true;
            Logger.debugLog("LogBlock is installed.");
        }
    }

    public static BukkitLunaticLib getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        GUIManager.closeAll();
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

    public static Version getServerVersion() {
        Logger.infoLog("Server version: " + Bukkit.getBukkitVersion());
        String string = Bukkit.getBukkitVersion().split("-")[0];
        string = string.replace(".", "_");
        string = "v" + string;

        try {
            return (Version) Class.forName("de.janschuri.lunaticlib.nms." + string + ".VersionImpl").getConstructor().newInstance();
        } catch (Exception e) {
            Logger.errorLog("Error: " + e);
            Logger.errorLog("Unsupported server version: " + string);
            return null;
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

    public static GUIManager getGUIManager() {
        return getInstance().guiManager;
    }

    public static Version getVersion() {
        return version;
    }
}
