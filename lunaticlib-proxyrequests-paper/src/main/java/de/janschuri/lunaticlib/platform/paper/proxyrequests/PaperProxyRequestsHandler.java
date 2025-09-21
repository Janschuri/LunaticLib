package de.janschuri.lunaticlib.platform.paper.proxyrequests;

import de.janschuri.lunaticlib.platform.paper.proxyrequests.listener.PluginMessageListener;
import de.janschuri.lunaticlib.platform.paper.sender.PaperSenderHandler;
import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler.IDENTIFIER;

public class PaperProxyRequestsHandler {

    private PaperProxyRequestsHandler() {}

    public static void initialize(PaperProxyRequestsAdapter adapter) {
        initialize(adapter, true);
    }

    public static void initialize(PaperProxyRequestsAdapter adapter, boolean initSenderHandler) {
        if (initSenderHandler) {
            PaperSenderHandler.initialize(adapter);
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
            PaperSenderHandler.shutdown();
        }
    }

    public static PaperProxyRequestsAdapter getAdapter() {
        return (PaperProxyRequestsAdapter) LunaticProxyRequestsHandler.getAdapter();
    }

    public static boolean isEnabled() {
        return LunaticProxyRequestsHandler.isEnabled();
    }

    private static void registerPluginMessageListener(JavaPlugin pluginInstance) {
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(pluginInstance, IDENTIFIER, new PluginMessageListener());
        Bukkit.getMessenger().registerOutgoingPluginChannel(pluginInstance, IDENTIFIER);
    }
}
