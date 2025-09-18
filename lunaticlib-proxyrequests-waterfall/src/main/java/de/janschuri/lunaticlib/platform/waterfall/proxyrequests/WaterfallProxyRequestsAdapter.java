package de.janschuri.lunaticlib.platform.waterfall.proxyrequests;

import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsAdapter;
import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.external.WaterfallVault;
import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.sender.WaterfallProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import static de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler.IDENTIFIER;

public class WaterfallProxyRequestsAdapter implements ProxyRequestsAdapter<ProxiedPlayer> {

    private Plugin plugin;
    private Vault vault;

    public WaterfallProxyRequestsAdapter(Plugin plugin) {
        this.plugin = plugin;
        this.vault = new WaterfallVault();
    }

    @Override
    public boolean sendPluginMessage(String server, byte[] message) {
        if (plugin.getProxy().getOnlineCount() == 0) {
            return false;
        }

        plugin.getProxy().getServerInfo(server).sendData(IDENTIFIER, message);
        return true;
    }

    @Override
    public boolean sendPluginMessage(byte[] message) {
        if (plugin.getProxy().getOnlineCount() == 0) {
            return false;
        }

        for (ServerInfo server : plugin.getProxy().getServers().values()) {
            server.sendData(IDENTIFIER, message);
        }

        return true;
    }

    @Override
    public Vault getVault() {
        return vault;
    }

    @Override
    public ProxyRequestsPlayerSender getPlayerSender(ProxiedPlayer proxiedPlayer) {
        return new WaterfallProxyRequestsPlayerSender(proxiedPlayer);
    }
}
