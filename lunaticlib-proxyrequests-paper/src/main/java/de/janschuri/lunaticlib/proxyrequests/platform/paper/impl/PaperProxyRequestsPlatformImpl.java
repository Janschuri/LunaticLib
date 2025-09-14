package de.janschuri.lunaticlib.proxyrequests.platform.paper.impl;

import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsPlatform;
import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.sender.platform.paper.impl.PaperSenderPlatformImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static de.janschuri.lunaticlib.proxyrequests.LunaticLibProxyRequests.IDENTIFIER;
import static de.janschuri.lunaticlib.proxyrequests.platform.paper.PaperLunaticLIbProxyRequests.getPluginInstance;

public class PaperProxyRequestsPlatformImpl extends PaperSenderPlatformImpl implements ProxyRequestsPlatform<JavaPlugin, CommandSender> {

    @Override
    public boolean sendPluginMessage(String server, byte[] message) {
        getPluginInstance().getServer().sendPluginMessage(getPluginInstance(), IDENTIFIER, message);
        return true;
    }

    @Override
    public boolean sendPluginMessage(byte[] message) {
        getPluginInstance().getServer().sendPluginMessage(getPluginInstance(), IDENTIFIER, message);
        return true;
    }

    @Override
    public Vault getVault() {
        return null;
    }

    @Override
    public ProxyRequestsPlayerSender getPlayerSender(UUID uuid) {
        return null;
    }
}
