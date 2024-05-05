package de.janschuri.lunaticlib;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import de.janschuri.lunaticlib.listener.velocity.MessageListener;
import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.utils.Platform;

import java.nio.file.Path;

@Plugin(
        id = "lunaticlib",
        name = "LunaticLib",
        version = "1.0.0",
        authors = "janschuri"
)
public class VelocityLunaticLib {

    private static ProxyServer proxy;
    private static Path dataDirectory;
    public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from(LunaticLib.IDENTIFIER);

    @Inject
    public VelocityLunaticLib(ProxyServer proxy, @DataDirectory Path dataDirectory) {
        VelocityLunaticLib.proxy = proxy;
        VelocityLunaticLib.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        LunaticLib.mode = Mode.PROXY;
        LunaticLib.platform = Platform.VELOCITY;
        LunaticLib.installedVault = true;

        proxy.getChannelRegistrar().register(IDENTIFIER);
        proxy.getEventManager().register(this, new MessageListener());

        LunaticLib.dataDirectory = dataDirectory;
        LunaticLib.onEnable();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        LunaticLib.onDisable();
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
}
