package de.janschuri.lunaticlib;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.nio.file.Path;

public class VelocityLunaticLib {

    private static ProxyServer proxy;
    private static Path dataDirectory;
    private static Logger logger;
    private static VelocityLunaticLib instance;
    public static MinecraftChannelIdentifier IDENTIFIER;

    public VelocityLunaticLib(ProxyServer proxy, Logger logger, Path dataDirectory, MinecraftChannelIdentifier IDENTIFIER) {
        VelocityLunaticLib.proxy = proxy;
        VelocityLunaticLib.logger = logger;
        VelocityLunaticLib.dataDirectory = dataDirectory;
        VelocityLunaticLib.instance = this;
        VelocityLunaticLib.IDENTIFIER = IDENTIFIER;
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    public static VelocityLunaticLib getInstance() {
        return instance;
    }

    public static void sendPluginMessage(byte[] message) {
        de.janschuri.lunaticlib.utils.Logger.debugLog("PluginMessage sent.");
        proxy.getAllServers().forEach(serverConnection -> serverConnection.sendPluginMessage(IDENTIFIER, message));
    }
}
