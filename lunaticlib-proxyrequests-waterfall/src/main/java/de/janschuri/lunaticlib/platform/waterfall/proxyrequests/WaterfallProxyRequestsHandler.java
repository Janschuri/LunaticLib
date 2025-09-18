package de.janschuri.lunaticlib.platform.waterfall.proxyrequests;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;
import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.listener.PluginMessageListener;
import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.listener.ServerConnectListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class WaterfallProxyRequestsHandler {

    private static WaterfallProxyRequestsAdapter adapter;
    private static Map<UUID, String> SKIN_CACHE = new HashMap<>();

    public static void enable(Plugin plugin) {
        if (adapter == null) {
            synchronized (WaterfallProxyRequestsHandler.class) {
                if (adapter == null) {
                    adapter = new WaterfallProxyRequestsAdapter(plugin);
                    LunaticProxyRequestsHandler.enable(adapter);
                }
            }
            registerPluginMessageListener(plugin);
            return;
        }

        throw new IllegalStateException("LunaticLibProxyRequests already is enabled.");
    }

    public static WaterfallProxyRequestsAdapter adapter() {
        return Objects.requireNonNull(adapter,"LunaticLibProxyRequests not enabled. Call WaterfallProxyRequestsHandler.enable(proxy) first.");
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
