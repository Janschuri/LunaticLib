package de.janschuri.lunaticlib.proxyrequests.platform.waterfall.listener;

import de.janschuri.lunaticlib.proxyrequests.platform.waterfall.WaterfallLunaticLibProxyRequests;
import de.janschuri.lunaticlib.proxyrequests.requests.GetSkinURLRequest;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ServerConnectListener implements Listener {

    @EventHandler
    public void onServerConnect(ServerConnectedEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        ProxyServer.getInstance().getScheduler().runAsync(WaterfallLunaticLibProxyRequests.getPluginInstance(), () -> {
            new GetSkinURLRequest().get(playerUUID)
                    .thenAccept(skinURL -> {
                        if (skinURL != null) {
                            WaterfallLunaticLibProxyRequests.setSkinCache(event.getPlayer().getUniqueId(), skinURL);
                        }
                    })
                    .exceptionally(throwable -> {
                        throwable.printStackTrace();
                        return null;
                    });
        });

    }
}
