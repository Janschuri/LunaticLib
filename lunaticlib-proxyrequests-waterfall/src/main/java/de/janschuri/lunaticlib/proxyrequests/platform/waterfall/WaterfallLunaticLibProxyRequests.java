package de.janschuri.lunaticlib.proxyrequests.platform.waterfall;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.janschuri.lunaticlib.proxyrequests.LunaticLibProxyRequests;
import de.janschuri.lunaticlib.proxyrequests.platform.waterfall.listener.PluginMessageListener;
import de.janschuri.lunaticlib.proxyrequests.platform.waterfall.listener.ServerConnectListener;
import net.md_5.bungee.api.plugin.Plugin;

public class WaterfallLunaticLibProxyRequests {

    private static Plugin pluginInstance;

    private static Map<UUID, String> SKIN_CACHE = new HashMap<>();

    public static void enable(Plugin pluginInstance) {
        WaterfallLunaticLibProxyRequests.pluginInstance = pluginInstance;

        registerListeners();

        LunaticLibProxyRequests.enable();
    }

    public static Plugin getPluginInstance() {
        return pluginInstance;
    }

    public static String getSkinCache(UUID uuid) {
        return SKIN_CACHE.get(uuid);
    }

    public static void setSkinCache(UUID uuid, String skin) {
        SKIN_CACHE.put(uuid, skin);
    }

    private static void registerListeners() {
        pluginInstance.getProxy().getPluginManager().registerListener(pluginInstance, new ServerConnectListener());
        pluginInstance.getProxy().getPluginManager().registerListener(pluginInstance, new PluginMessageListener());
    }
}
