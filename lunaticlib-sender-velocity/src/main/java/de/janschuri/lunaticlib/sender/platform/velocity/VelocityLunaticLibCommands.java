package de.janschuri.lunaticlib.sender.platform.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import de.janschuri.lunaticlib.sender.LunaticLibSender;
import de.janschuri.lunaticlib.sender.platform.velocity.impl.VelocitySenderPlatformImpl;

public final class VelocityLunaticLibCommands extends LunaticLibSender {

    private static VelocitySenderPlatformImpl platform;
    private static ProxyServer proxy;

    public static void enable(ProxyServer proxy) {
        if (platform == null) {
            platform = new VelocitySenderPlatformImpl();
            setPlatform(platform);
        }

        VelocityLunaticLibCommands.proxy = proxy;
    }

    public static ProxyServer getProxy() {
        return proxy;
    }
}