package de.janschuri.lunaticlib.platform.velocity.proxyrequests;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import de.janschuri.lunaticlib.platform.velocity.proxyrequests.listener.PluginMessageListener;
import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;

import java.util.Objects;

import static de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler.IDENTIFIER;

public class VelocityProxyRequestsHandler {

    public static final MinecraftChannelIdentifier MINECRAFT_CHANNEL_IDENTIFIER = MinecraftChannelIdentifier.from(IDENTIFIER);

    private static VelocityProxyRequestsAdapter adapter;

    public static void enable(ProxyServer proxy, Object pluginInstance) {
        if (adapter == null) {
            synchronized (VelocityProxyRequestsHandler.class) {
                if (adapter == null) {
                    adapter = new VelocityProxyRequestsAdapter(proxy);
                    LunaticProxyRequestsHandler.enable(adapter);
                }
            }
            registerPluginMessageListener(pluginInstance);
            return;
        }

        throw new IllegalStateException("LunaticLibProxyRequests already is enabled.");
    }

    public static VelocityProxyRequestsAdapter adapter() {
        return Objects.requireNonNull(adapter,"LunaticLibProxyRequests not enabled. Call VelocityProxyRequestsHandler.enable(proxy) first.");
    }


    public static void registerPluginMessageListener(Object pluginInstance) {
        adapter().getProxy().getChannelRegistrar().register(MINECRAFT_CHANNEL_IDENTIFIER);
        adapter().getProxy().getEventManager().register(pluginInstance, new PluginMessageListener());
    }
}
