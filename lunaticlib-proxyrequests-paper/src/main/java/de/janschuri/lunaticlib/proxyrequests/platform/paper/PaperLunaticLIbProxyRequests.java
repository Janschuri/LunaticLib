package de.janschuri.lunaticlib.proxyrequests.platform.paper;

import de.janschuri.lunaticlib.proxyrequests.LunaticLibProxyRequests;
import de.janschuri.lunaticlib.proxyrequests.platform.paper.listener.PaperPluginMessageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static de.janschuri.lunaticlib.proxyrequests.LunaticLibProxyRequests.IDENTIFIER;

public class PaperLunaticLIbProxyRequests {

    private static JavaPlugin plugin;

    public static void enable(JavaPlugin plugin) {
        registerPluginMessageListener(plugin);
        PaperLunaticLIbProxyRequests.plugin = plugin;

        LunaticLibProxyRequests.enable();
    }

    private static void registerPluginMessageListener(JavaPlugin pluginInstance) {
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(pluginInstance, IDENTIFIER, new PaperPluginMessageListener());
        Bukkit.getMessenger().registerOutgoingPluginChannel(pluginInstance, IDENTIFIER);
    }

    public static JavaPlugin getPluginInstance() {
        return plugin;
    }
}
