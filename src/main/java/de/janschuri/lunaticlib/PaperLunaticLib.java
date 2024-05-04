package de.janschuri.lunaticlib;

import de.janschuri.lunaticlib.external.Vault;
import de.janschuri.lunaticlib.listener.paper.MessageListener;
import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.utils.Platform;
import de.janschuri.lunaticlib.utils.Utils;
import de.janschuri.lunaticlib.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static de.janschuri.lunaticlib.LunaticLib.IDENTIFIER;

public class PaperLunaticLib  extends JavaPlugin {
    private static PaperLunaticLib instance;
    @Override
    public void onEnable() {
        instance = this;

        LunaticLib.mode = Mode.STANDALONE;
        LunaticLib.platform = Platform.PAPER;

        getServer().getMessenger().registerIncomingPluginChannel(this, IDENTIFIER, new MessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, IDENTIFIER);

        LunaticLib.setDataDirectory(getDataFolder().toPath());
        LunaticLib.loadConfig();

        LunaticLib.registerRequests();

        if (Utils.classExists("net.milkbowl.vault.economy.Economy")) {
            LunaticLib.installedVault = true;
            new Vault();
        }

        Logger.infoLog("LunaticLib enabled.");
    }

    public static PaperLunaticLib getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        LunaticLib.unregisterRequests();
        de.janschuri.lunaticlib.logger.Logger.infoLog("LunaticLib disabled.");
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
