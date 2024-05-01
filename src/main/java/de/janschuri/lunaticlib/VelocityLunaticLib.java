package de.janschuri.lunaticlib;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.janschuri.lunaticlib.listener.velocity.MessageListener;
import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.utils.Platform;
import de.janschuri.lunaticlib.utils.logger.VelocityLogger;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Set;

@Plugin(
        id = "lunaticlib",
        name = "LunaticLib",
        version = "1.0-SNAPSHOT",
        authors = "janschuri"
)
public class VelocityLunaticLib {

    private static ProxyServer proxy;
    private static Path dataDirectory;
    private static Logger logger;
    private static CommandManager commandManager;
    public static MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("lunaticlib:futurerequests");

    @Inject
    public VelocityLunaticLib(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        VelocityLunaticLib.proxy = proxy;
        VelocityLunaticLib.logger = logger;
        VelocityLunaticLib.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        LunaticLib.mode = Mode.PROXY;
        LunaticLib.platform = Platform.VELOCITY;

        proxy.getChannelRegistrar().register(IDENTIFIER);
        proxy.getEventManager().register(this, new MessageListener());

        new de.janschuri.lunaticlib.utils.logger.Logger(new VelocityLogger(logger));

        LunaticLib.registerRequests();

        de.janschuri.lunaticlib.utils.logger.Logger.infoLog("LunaticLib enabled.");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        LunaticLib.unregisterRequests();
        de.janschuri.lunaticlib.utils.logger.Logger.infoLog("LunaticLib disabled.");
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    static boolean sendPluginMessage(String serverName, byte[] message) {
        if (getProxy().getPlayerCount() == 0) {
            return false;
        }

        proxy.getServer(serverName).ifPresent(serverConnection -> serverConnection.sendPluginMessage(IDENTIFIER, message));
        return true;
    }

    static boolean sendPluginMessage(byte[] message) {
        if (getProxy().getPlayerCount() == 0) {
            return false;
        }

        proxy.getAllServers().forEach(serverConnection -> serverConnection.sendPluginMessage(IDENTIFIER, message));

        return true;
    }

    public static void sendConsoleCommand(String command) {
        proxy.getCommandManager().executeAsync(proxy.getConsoleCommandSource(), command);
    }

    static Player getRandomPlayer() {
        return proxy.getAllPlayers().iterator().next();
    }
}
