package de.janschuri.lunaticlib.platform.bukkit;

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
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

import static de.janschuri.lunaticlib.common.LunaticLib.IDENTIFIER;

public class BukkitLunaticLib extends JavaPlugin {
    private static BukkitLunaticLib instance;
    static boolean installedVault = false;
    static boolean installedLogBlock = false;
    @Override
    public void onEnable() {
        instance = this;

        AdventureAPI.initialize(this);

        getServer().getMessenger().registerIncomingPluginChannel(this, IDENTIFIER, new MessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, IDENTIFIER);


        if (Utils.classExists("net.milkbowl.vault.economy.Economy")) {
            BukkitLunaticLib.installedVault = true;
            new VaultImpl();
        }

        if (Utils.classExists("de.diddiz.LogBlock.LogBlock")) {
            BukkitLunaticLib.installedLogBlock = true;
            new LogBlock();
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
}
