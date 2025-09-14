package de.janschuri.lunaticlib.proxyrequests.platform.velocity.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsPlatform;
import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.sender.platform.velocity.impl.VelocitySenderPlatformImpl;

import java.util.UUID;

public class VelocityProxyRequestsPlatformImpl extends VelocitySenderPlatformImpl implements ProxyRequestsPlatform<PluginContainer, CommandSource> {

    @Override
    public boolean sendPluginMessage(String server, byte[] message) {
        return false;
    }

    @Override
    public boolean sendPluginMessage(byte[] message) {
        return false;
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
