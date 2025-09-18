package de.janschuri.lunaticlib.platform.velocity.proxyrequests;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.janschuri.lunaticlib.platform.velocity.proxyrequests.external.VelocityVault;
import de.janschuri.lunaticlib.platform.velocity.proxyrequests.sender.VelocityProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsAdapter;
import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocitySenderAdapter;

import java.util.UUID;

import static de.janschuri.lunaticlib.platform.velocity.proxyrequests.VelocityProxyRequestsHandler.MINECRAFT_CHANNEL_IDENTIFIER;

public class VelocityProxyRequestsAdapter implements ProxyRequestsAdapter<Player> {

    private final ProxyServer proxy;
    private final Vault vault;

    public VelocityProxyRequestsAdapter(ProxyServer proxy) {
        this.proxy = proxy;
        this.vault = new VelocityVault();
    }

    @Override
    public boolean sendPluginMessage(String server, byte[] message) {
        if (getProxy().getPlayerCount() == 0) {
            return false;
        }

        proxy.getServer(server).ifPresent(serverConnection -> serverConnection.sendPluginMessage(MINECRAFT_CHANNEL_IDENTIFIER, message));
        return true;
    }

    @Override
    public boolean sendPluginMessage(byte[] message) {
        if (getProxy().getPlayerCount() == 0) {
            return false;
        }

        getProxy().getAllServers().forEach(serverConnection -> serverConnection.sendPluginMessage(MINECRAFT_CHANNEL_IDENTIFIER, message));
        return true;
    }

    @Override
    public Vault getVault() {
        return vault;
    }

    @Override
    public ProxyRequestsPlayerSender getPlayerSender(Player player) {
        return new VelocityProxyRequestsPlayerSender(player);
    }

    public ProxyServer getProxy() {
        return proxy;
    }
}
