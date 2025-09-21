package de.janschuri.lunaticlib.platform.waterfall.proxyrequests;

import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.sender.WaterfallProxyRequestsSender;
import de.janschuri.lunaticlib.platform.waterfall.sender.WaterfallSenderAdapter;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsAdapter;
import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.external.WaterfallVault;
import de.janschuri.lunaticlib.platform.waterfall.proxyrequests.sender.WaterfallProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;

import static de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler.IDENTIFIER;

public class WaterfallProxyRequestsAdapter extends WaterfallSenderAdapter implements ProxyRequestsAdapter<CommandSender> {

    private final Plugin plugin;
    private final Vault vault;

    public WaterfallProxyRequestsAdapter(Plugin plugin) {
        super(plugin);
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
    public ProxyRequestsSender getSender(CommandSender sender) {
        if (sender instanceof ProxiedPlayer player) {
            return new WaterfallProxyRequestsPlayerSender(player);
        }

        return new WaterfallProxyRequestsSender(sender);
    }

    @Override
    public ProxyRequestsPlayerSender getPlayerSender(UUID uuid) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(uuid);
        if (player != null) {
            return new WaterfallProxyRequestsPlayerSender(player);
        }
        return null;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
