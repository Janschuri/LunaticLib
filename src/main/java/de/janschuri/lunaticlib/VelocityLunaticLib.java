package de.janschuri.lunaticlib;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Velocity {

    private static ProxyServer proxy;
    private static Path dataDirectory;
    private static Logger logger;
    private static Velocity instance;
    public static MinecraftChannelIdentifier IDENTIFIER;

    public Velocity(ProxyServer proxy, Logger logger, Path dataDirectory, MinecraftChannelIdentifier IDENTIFIER) {
        Velocity.proxy = proxy;
        Velocity.logger = logger;
        Velocity.dataDirectory = dataDirectory;
        Velocity.instance = this;
        Velocity.IDENTIFIER = IDENTIFIER;
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    public static Velocity getInstance() {
        return instance;
    }

    public static void sendPluginMessage(byte[] message) {
        de.janschuri.lunaticlib.utils.Logger.debugLog("PluginMessage sent.");
        proxy.getAllServers().forEach(serverConnection -> serverConnection.sendPluginMessage(IDENTIFIER, message));
    }
}
