package de.janschuri.lunaticlib;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import de.janschuri.lunaticlib.listener.velocity.MessageListener;
import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.utils.Platform;
import de.janschuri.lunaticlib.utils.logger.VelocityLogger;
import org.slf4j.Logger;

import java.nio.file.Path;

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
    private static VelocityLunaticLib instance;
    public static MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.from("lunaticlib:proxy");

    @Inject
    public VelocityLunaticLib(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        VelocityLunaticLib.proxy = proxy;
        VelocityLunaticLib.logger = logger;
        VelocityLunaticLib.dataDirectory = dataDirectory;
        VelocityLunaticLib.instance = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        LunaticLib.mode = Mode.PROXY;
        LunaticLib.platform = Platform.VELOCITY;
        new de.janschuri.lunaticlib.utils.logger.Logger(new VelocityLogger(logger));
        proxy.getChannelRegistrar().register(IDENTIFIER);
        proxy.getEventManager().register(this, new MessageListener());
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    public static VelocityLunaticLib getInstance() {
        return instance;
    }

    static void sendPluginMessage(byte[] message) {
        de.janschuri.lunaticlib.utils.logger.Logger.debugLog("sending plugin message");
        proxy.getAllServers().forEach(serverConnection -> serverConnection.sendPluginMessage(IDENTIFIER, message));
    }
}
