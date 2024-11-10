package de.janschuri.lunaticlib.platform.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import de.janschuri.lunaticlib.platform.Platform;
import de.janschuri.lunaticlib.platform.velocity.external.Metrics;
import de.janschuri.lunaticlib.platform.velocity.listener.MessageListener;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.utils.Mode;

import java.nio.file.Path;

import static de.janschuri.lunaticlib.common.LunaticLib.IDENTIFIER;

@Plugin(
        id = "lunaticlib",
        name = "LunaticLib",
        version = "1.2.2",
        authors = "janschuri"
)
public class VelocityLunaticLib {

    private static ProxyServer proxy;
    private static Path dataDirectory;
    public static final MinecraftChannelIdentifier MINECRAFT_CHANNEL_IDENTIFIER = MinecraftChannelIdentifier.from(IDENTIFIER);
    private final Metrics.Factory metricsFactory;

    @Inject
    public VelocityLunaticLib(ProxyServer proxy, @DataDirectory Path dataDirectory, Metrics.Factory metricsFactory) {
        VelocityLunaticLib.proxy = proxy;
        VelocityLunaticLib.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxy.getChannelRegistrar().register(MINECRAFT_CHANNEL_IDENTIFIER);
        proxy.getEventManager().register(this, new MessageListener());

        int pluginId = 21915;
        Metrics metrics = metricsFactory.make(this, pluginId);

        Path dataDirectory = VelocityLunaticLib.dataDirectory;
        Mode mode = Mode.PROXY;
        Platform platform = new PlatformImpl();

        LunaticLib.onEnable(dataDirectory, mode, platform);
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

        proxy.getServer(serverName).ifPresent(serverConnection -> serverConnection.sendPluginMessage(MINECRAFT_CHANNEL_IDENTIFIER, message));
        return true;
    }

    static boolean sendPluginMessage(byte[] message) {
        if (getProxy().getPlayerCount() == 0) {
            return false;
        }


        getProxy().getAllServers().forEach(serverConnection -> serverConnection.sendPluginMessage(MINECRAFT_CHANNEL_IDENTIFIER, message));
        return true;
    }


    }
