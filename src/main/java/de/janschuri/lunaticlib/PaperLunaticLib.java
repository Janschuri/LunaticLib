package de.janschuri.lunaticlib;

import de.janschuri.lunaticlib.external.AdventureAPI;
import de.janschuri.lunaticlib.external.Vault;
import de.janschuri.lunaticlib.listener.paper.MessageListener;
import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.utils.Platform;
import de.janschuri.lunaticlib.utils.Utils;
import de.janschuri.lunaticlib.logger.Logger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static de.janschuri.lunaticlib.LunaticLib.IDENTIFIER;

public class PaperLunaticLib  extends JavaPlugin {
    private static PaperLunaticLib instance;
    @Override
    public void onEnable() {
        instance = this;

        LunaticLib.mode = Mode.STANDALONE;
        LunaticLib.platform = Platform.BUKKIT;

        AdventureAPI.initialize(this);

        getServer().getMessenger().registerIncomingPluginChannel(this, IDENTIFIER, new MessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, IDENTIFIER);

        LunaticLib.dataDirectory = getDataFolder().toPath();

        if (Utils.classExists("net.milkbowl.vault.economy.Economy")) {
            LunaticLib.installedVault = true;
            new Vault();
        }

        LunaticLib.onEnable();
    }

    public static PaperLunaticLib getInstance() {
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
