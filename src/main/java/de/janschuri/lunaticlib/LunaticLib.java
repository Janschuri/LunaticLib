package de.janschuri.lunaticlib;

import de.janschuri.lunaticlib.listener.paper.MessageListener;
import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.utils.logger.BukkitLogger;
import de.janschuri.lunaticlib.utils.logger.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaticLib extends JavaPlugin {

    private static final String IDENTIFIER = "lunaticlib:proxy";
    static Mode mode = Mode.STANDALONE;
    private static LunaticLib instance;

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
        getInstance().getServer().sendPluginMessage(getInstance(), IDENTIFIER, message);
    }
}
