package de.janschuri.lunaticlib;

import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.utils.Platform;
import de.janschuri.lunaticlib.utils.logger.BungeeLogger;
import de.janschuri.lunaticlib.utils.logger.Logger;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Set;

public class BungeeLunaticLib extends Plugin {
    private static BungeeLunaticLib instance;

    @Override
    public void onEnable() {
        instance = this;

        LunaticLib.mode = Mode.PROXY;
        LunaticLib.platform = Platform.BUNGEE;

        getProxy().registerChannel(LunaticLib.IDENTIFIER);

        new Logger(new BungeeLogger(this));

        LunaticLib.registerRequests();

        Logger.infoLog("LunaticLib enabled.");
    }

    @Override
    public void onDisable() {
        LunaticLib.unregisterRequests();
        Logger.infoLog("LunaticLib disabled.");
    }

    public static BungeeLunaticLib getInstance() {
        return instance;
    }

    static boolean sendPluginMessage(String serverName, byte[] message) {
        if (getInstance().getProxy().getOnlineCount() == 0) {
            return false;
        }

        getInstance().getProxy().getServerInfo(serverName).sendData(LunaticLib.IDENTIFIER, message);
        return true;
    }

    static boolean sendPluginMessage(byte[] message) {
        if (getInstance().getProxy().getOnlineCount() == 0) {
            return false;
        }

        for (ServerInfo server : getInstance().getProxy().getServers().values()) {
            server.sendData(LunaticLib.IDENTIFIER, message);
        }

        return true;
    }

    public static void sendConsoleCommand(String command) {
        getInstance().getProxy().getPluginManager().dispatchCommand(getInstance().getProxy().getConsole(), command);
    }

    static ProxiedPlayer getRandomPlayer() {
        return getInstance().getProxy().getPlayers().iterator().next();
    }
}
