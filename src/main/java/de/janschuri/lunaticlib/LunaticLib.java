package de.janschuri.lunaticlib;

import de.janschuri.lunaticlib.listener.paper.MessageListener;
import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.utils.Platform;
import de.janschuri.lunaticlib.utils.logger.BukkitLogger;
import de.janschuri.lunaticlib.utils.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaticLib extends JavaPlugin {

    static final String IDENTIFIER = "lunaticlib:proxy";
    static Mode mode = Mode.STANDALONE;
    static Platform platform = Platform.PAPER;
    private static LunaticLib instance;

    public static Platform getPlatform() {
        return platform;
    }

    @Override
        public void onEnable() {
            instance = this;

            getServer().getMessenger().registerIncomingPluginChannel(this, IDENTIFIER, new MessageListener());
            getServer().getMessenger().registerOutgoingPluginChannel(this, IDENTIFIER);
            new Logger (new BukkitLogger(this));
            Logger.infoLog("LunaticLib enabled");
        }

        @Override
        public void onDisable() {
            // Plugin shutdown logic
        }

    public static LunaticLib getInstance() {
        return instance;
    }

    public static void sendPluginMessage(byte[] message) {
        switch (platform) {
            case PAPER:
                getInstance().getServer().sendPluginMessage(getInstance(), IDENTIFIER, message);
                break;
            case VELOCITY:
                VelocityLunaticLib.sendPluginMessage(message);
                break;
            case BUNGEE:
                BungeeLunaticLib.sendPluginMessage(message);
                break;
            default:
                Logger.errorLog("Platform not supported");
        }


    }

    private static void disable() {
        Logger.errorLog("Disabling LunaticLib...");
        Bukkit.getServer().getPluginManager().disablePlugin(getInstance());
    }
}
