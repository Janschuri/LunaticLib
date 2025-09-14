package de.janschuri.lunaticlib.proxyrequests.platform.waterfall.impl;

import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsPlatform;
import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.proxyrequests.platform.waterfall.WaterfallLunaticLibProxyRequests;
import de.janschuri.lunaticlib.proxyrequests.platform.waterfall.external.VaultImpl;
import de.janschuri.lunaticlib.proxyrequests.platform.waterfall.impl.sender.ProxyRequestsPlayerSenderImpl;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.sender.platform.waterfall.impl.PlatformImpl;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;

import static de.janschuri.lunaticlib.proxyrequests.LunaticLibProxyRequests.IDENTIFIER;

public class ProxyRequestsPlatformImpl extends PlatformImpl implements ProxyRequestsPlatform<Plugin, CommandSender> {

    @Override
    public boolean sendPluginMessage(String server, byte[] message) {
        if (WaterfallLunaticLibProxyRequests.getPluginInstance().getProxy().getOnlineCount() == 0) {
            return false;
        }

        WaterfallLunaticLibProxyRequests.getPluginInstance().getProxy().getServerInfo(server).sendData(IDENTIFIER, message);
        return true;
    }

    @Override
    public boolean sendPluginMessage(byte[] message) {
        if (WaterfallLunaticLibProxyRequests.getPluginInstance().getProxy().getOnlineCount() == 0) {
            return false;
        }

        for (ServerInfo server : WaterfallLunaticLibProxyRequests.getPluginInstance().getProxy().getServers().values()) {
            server.sendData(IDENTIFIER, message);
        }

        return true;
    }

    @Override
    public Vault getVault() {
        return new VaultImpl();
    }

    @Override
    public ProxyRequestsPlayerSender getPlayerSender(UUID uuid) {
        return new ProxyRequestsPlayerSenderImpl(uuid);
    }
}
