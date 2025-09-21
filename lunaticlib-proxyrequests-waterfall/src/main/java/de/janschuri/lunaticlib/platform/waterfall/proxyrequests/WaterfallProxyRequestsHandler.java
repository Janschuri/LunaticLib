package de.janschuri.lunaticlib.platform.waterfall.proxyrequests;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.janschuri.lunaticlib.platform.waterfall.sender.WaterfallSenderHandler;
import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;
import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.listener.PluginMessageListener;
import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.listener.ServerConnectListener;
import de.janschuri.lunaticlib.utils.SingletonHolder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import static de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler.IDENTIFIER;

public class WaterfallProxyRequestsHandler {

    private static Map<UUID, String> SKIN_CACHE = new HashMap<>();

    private WaterfallProxyRequestsHandler() {}

    public static void initialize(WaterfallProxyRequestsAdapter adapter) {
        initialize(adapter, true);
    }

    public static void initialize(WaterfallProxyRequestsAdapter adapter, boolean initSenderHandler) {
        if (initSenderHandler) {
            WaterfallSenderHandler.initialize(adapter);
        }

        LunaticProxyRequestsHandler.initialize(adapter);
        adapter.getPlugin().getProxy().registerChannel(IDENTIFIER);
        registerPluginMessageListener(adapter.getPlugin());
    }

    public static void shutdown() {
        shutdown(true);
    }

    public static void shutdown(boolean shutdownSenderHandler) {
        ProxyServer.getInstance().unregisterChannel(IDENTIFIER);
        SKIN_CACHE.clear();
        LunaticProxyRequestsHandler.shutdown();

        if (shutdownSenderHandler) {
            WaterfallSenderHandler.shutdown();
        }
    }

    public static WaterfallProxyRequestsAdapter getAdapter() {
        return (WaterfallProxyRequestsAdapter) LunaticProxyRequestsHandler.getAdapter();
    }

    public static boolean isEnabled() {
        return LunaticProxyRequestsHandler.isEnabled();
    }

    public static String getSkinCache(UUID uuid) {
        return SKIN_CACHE.get(uuid);
    }

    public static void setSkinCache(UUID uuid, String skin) {
        SKIN_CACHE.put(uuid, skin);
    }

    private static void registerPluginMessageListener(Plugin pluginInstance) {
        pluginInstance.getProxy().getPluginManager().registerListener(pluginInstance, new ServerConnectListener(pluginInstance));
        pluginInstance.getProxy().getPluginManager().registerListener(pluginInstance, new PluginMessageListener());
    }
}
