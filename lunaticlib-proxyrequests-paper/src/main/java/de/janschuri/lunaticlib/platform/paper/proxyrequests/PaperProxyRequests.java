package de.janschuri.lunaticlib.platform.paper.proxyrequests;

import de.janschuri.lunaticlib.platform.paper.proxyrequests.listener.PluginMessageListener;
import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsAdapter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler.IDENTIFIER;

public class PaperProxyRequests {

    private static ProxyRequestsAdapter adapter;

    public static void enable(JavaPlugin plugin) {
        if (adapter == null) {
            synchronized (PaperProxyRequests.class) {
                if (adapter == null) {
                    adapter = new PaperProxyRequestsAdapter(plugin);
                    LunaticProxyRequestsHandler.enable(adapter);
                }
            }
        }

        registerPluginMessageListener(plugin);
    }

    private static void registerPluginMessageListener(JavaPlugin pluginInstance) {
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(pluginInstance, IDENTIFIER, new PluginMessageListener());
        Bukkit.getMessenger().registerOutgoingPluginChannel(pluginInstance, IDENTIFIER);
    }
}
