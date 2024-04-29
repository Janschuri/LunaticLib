package de.janschuri.lunaticlib;

import de.janschuri.lunaticlib.listener.paper.MessageListener;
import de.janschuri.lunaticlib.utils.logger.BukkitLogger;
import de.janschuri.lunaticlib.utils.logger.BungeeLogger;
import de.janschuri.lunaticlib.utils.logger.Logger;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Bukkit;

public class BungeeLunaticLib extends Plugin {
    private static BungeeLunaticLib instance;

    @Override
    public void onEnable() {
        instance = this;

        getProxy().registerChannel(LunaticLib.IDENTIFIER);
        new Logger(new BungeeLogger(this));
        Logger.infoLog("LunaticLib enabled");
    }

    public static BungeeLunaticLib getInstance() {
        return instance;
    }

    static void sendPluginMessage(byte[] message) {
        getInstance().getProxy().getServers().values().forEach(serverInfo -> serverInfo.sendData(LunaticLib.IDENTIFIER, message));
    }
}