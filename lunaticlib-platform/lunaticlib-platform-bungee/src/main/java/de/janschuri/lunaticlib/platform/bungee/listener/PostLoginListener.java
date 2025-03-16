package de.janschuri.lunaticlib.platform.bungee.listener;

import de.janschuri.lunaticlib.common.futurerequests.requests.GetSkinURLRequest;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.common.utils.Utils;
import de.janschuri.lunaticlib.platform.bungee.BungeeLunaticLib;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PostLoginListener implements Listener {

    @EventHandler
    public void onPostLogin(ServerConnectedEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        ProxyServer.getInstance().getScheduler().runAsync(BungeeLunaticLib.getInstance(), () -> {
            new GetSkinURLRequest().get(playerUUID)
                    .thenAccept(skinURL -> {
                        if (skinURL != null) {
                            Logger.debugLog(String.format("Skin URL: %s", skinURL));
                            BungeeLunaticLib.setSkinCache(event.getPlayer().getUniqueId(), skinURL);
                        }
                    })
                    .exceptionally(throwable -> {
                        throwable.printStackTrace();
                        return null;
                    });
        });

    }
}
