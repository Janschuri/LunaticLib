package de.janschuri.lunaticlib.proxyrequests.platform.velocity;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import de.janschuri.lunaticlib.proxyrequests.LunaticLibProxyRequests;
import de.janschuri.lunaticlib.proxyrequests.platform.velocity.listener.VelocityPluginMessageListener;

import static de.janschuri.lunaticlib.proxyrequests.LunaticLibProxyRequests.IDENTIFIER;

public class VelocityLunaticLibProxyRequests {

    public static final MinecraftChannelIdentifier MINECRAFT_CHANNEL_IDENTIFIER = MinecraftChannelIdentifier.from(IDENTIFIER);

    private static ProxyServer proxy;

    public static void enable(ProxyServer proxy, PluginContainer pluginContainer) {
        registerPluginMessageListener(pluginContainer);
        VelocityLunaticLibProxyRequests.proxy = proxy;

        LunaticLibProxyRequests.enable();
    }


    public static void registerPluginMessageListener(PluginContainer plugin) {
        proxy.getChannelRegistrar().register(MINECRAFT_CHANNEL_IDENTIFIER);
        proxy.getEventManager().register(plugin, new VelocityPluginMessageListener());
    }

    public static ProxyServer getProxy() {
        return proxy;
    }
}
