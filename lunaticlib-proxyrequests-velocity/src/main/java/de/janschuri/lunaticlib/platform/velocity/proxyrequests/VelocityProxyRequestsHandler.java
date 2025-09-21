package de.janschuri.lunaticlib.platform.velocity.proxyrequests;

import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import de.janschuri.lunaticlib.platform.velocity.proxyrequests.listener.PluginMessageListener;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocitySenderHandler;
import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;

import static de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler.IDENTIFIER;

public class VelocityProxyRequestsHandler {

    public static final MinecraftChannelIdentifier MINECRAFT_CHANNEL_IDENTIFIER = MinecraftChannelIdentifier.from(IDENTIFIER);

    private VelocityProxyRequestsHandler() {}

    public static void initialize(VelocityProxyRequestsAdapter adapter) {
        initialize(adapter, true);
    }

    public static void initialize(VelocityProxyRequestsAdapter adapter, boolean initSenderHandler) {
        if (initSenderHandler) {
            VelocitySenderHandler.initialize(adapter);
        }

        LunaticProxyRequestsHandler.initialize(adapter);
        registerPluginMessageListener(adapter.getPlugin());
    }


    public static void shutdown() {
        shutdown(true);
    }

    public static void shutdown(boolean shutdownSenderHandler) {
        LunaticProxyRequestsHandler.shutdown();

        if (shutdownSenderHandler) {
            VelocitySenderHandler.shutdown();
        }
    }

    public static VelocityProxyRequestsAdapter getAdapter() {
        return (VelocityProxyRequestsAdapter) LunaticProxyRequestsHandler.getAdapter();
    }

    public static boolean isEnabled() {
        return LunaticProxyRequestsHandler.isEnabled();
    }

    public static void registerPluginMessageListener(Object pluginInstance) {
        getAdapter().getProxy().getChannelRegistrar().register(MINECRAFT_CHANNEL_IDENTIFIER);
        getAdapter().getProxy().getEventManager().register(pluginInstance, new PluginMessageListener());
    }
}
