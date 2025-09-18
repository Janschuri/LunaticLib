package de.janschuri.lunaticlib.platform.waterfall.proxyrequests.listener;

import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.WaterfallProxyRequestsHandler;
import de.janschuri.lunaticlib.proxyrequests.requests.GetSkinURLRequest;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ServerConnectListener implements Listener {

    private final Plugin plugin;

    public ServerConnectListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerConnect(ServerConnectedEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
            new GetSkinURLRequest().get(playerUUID)
                    .thenAccept(skinURL -> {
                        if (skinURL != null) {
                            WaterfallProxyRequestsHandler.setSkinCache(event.getPlayer().getUniqueId(), skinURL);
                        }
                    })
                    .exceptionally(throwable -> {
                        throwable.printStackTrace();
                        return null;
                    });
        });

    }
}
