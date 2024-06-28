package de.janschuri.lunaticlib.platform.bukkit;

import de.janschuri.lunaticlib.platform.Vault;
import de.janschuri.lunaticlib.platform.bukkit.external.AdventureAPI;
import de.janschuri.lunaticlib.platform.bukkit.external.LogBlock;
import de.janschuri.lunaticlib.platform.bukkit.external.Metrics;
import de.janschuri.lunaticlib.platform.bukkit.external.VaultImpl;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.DecisionGUI;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIListener;
import de.janschuri.lunaticlib.platform.bukkit.inventorygui.GUIManager;
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

    private GUIManager guiManager;

    @Override
    public void onEnable() {
        instance = this;
        version = getServerVersion();
        guiManager = new GUIManager();

        if (version == VersionEnum.UNKNOWN) {
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

    public static VersionEnum getServerVersion() {
        if (version != null) {
            return version;
        }

        String versionName = Bukkit.getServer().getBukkitVersion();

        String versionString = convertVersion(versionName);

        try {
            Logger.infoLog("Server version: " + versionString);
            return VersionEnum.valueOf(versionString);
        } catch (IllegalArgumentException e) {
            Logger.errorLog("Unsupported server version: " + versionName);
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

    public static String convertVersion(String version) {
        String regex = "^(\\d+)\\.(\\d+)(?:\\.(\\d+))?-(R\\d+).*-SNAPSHOT$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(version);

        if (matcher.find()) {
            String major = matcher.group(1);
            String minor = matcher.group(2);
            String patch = matcher.group(3);

            StringBuilder newVersion = new StringBuilder();
            newVersion.append("v").append(major).append("_").append(minor);
            if (patch != null) {
                newVersion.append("_R").append(patch);
            } else {
                newVersion.append("_R").append("1");
            }

            return newVersion.toString();
        } else {
            throw new IllegalArgumentException("Invalid version format: " + version);
        }
    }

    public static GUIManager getGUIManager() {
        return getInstance().guiManager;
    }
}
