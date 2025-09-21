package de.janschuri.lunaticlib.platform.waterfall;

import de.janschuri.lunaticlib.platform.waterfall.commands.WaterfallCommandAdapter;
import de.janschuri.lunaticlib.platform.waterfall.commands.WaterfallCommandHandler;
import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.WaterfallProxyRequestsAdapter;
import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.WaterfallProxyRequestsHandler;
import de.janschuri.lunaticlib.platform.waterfall.sender.WaterfallSenderAdapter;
import de.janschuri.lunaticlib.platform.waterfall.sender.WaterfallSenderHandler;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsAdapter;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WaterfallLunaticLib extends Plugin {
    private static WaterfallLunaticLib instance;
    private static Map<UUID, String> SKIN_CACHE = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        WaterfallSenderAdapter senderAdapter = new WaterfallSenderAdapter(instance);
        WaterfallSenderHandler.initialize(senderAdapter);

        WaterfallCommandAdapter commandAdapter = new WaterfallCommandAdapter(instance);
        WaterfallCommandHandler.initialize(commandAdapter, false);

        WaterfallProxyRequestsAdapter proxyRequestsAdapter = new WaterfallProxyRequestsAdapter(instance);
        WaterfallProxyRequestsHandler.initialize(proxyRequestsAdapter, false);

        int pluginId = 21919;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        WaterfallSenderHandler.shutdown();
        WaterfallCommandHandler.shutdown();
        WaterfallProxyRequestsHandler.shutdown();
    }

    public static WaterfallLunaticLib getInstance() {
        return instance;
    }

    public static String getSkinCache(UUID uuid) {
        return SKIN_CACHE.get(uuid);
    }

    public static void setSkinCache(UUID uuid, String skin) {
        SKIN_CACHE.put(uuid, skin);
    }
}
