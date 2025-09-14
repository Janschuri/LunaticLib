package de.janschuri.lunaticlib.proxyrequests;

import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.sender.platform.SenderPlatform;

import java.util.UUID;

public interface ProxyRequestsPlatform<P, T> extends SenderPlatform<P, T> {

    @Override
    ProxyRequestsPlayerSender getPlayerSender(UUID uuid);

    boolean sendPluginMessage(String server, byte[] message);
    boolean sendPluginMessage(byte[] message);

    Vault getVault();
}
