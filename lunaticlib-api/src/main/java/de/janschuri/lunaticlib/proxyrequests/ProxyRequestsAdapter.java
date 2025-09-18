package de.janschuri.lunaticlib.proxyrequests;

import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;

import java.util.UUID;

public interface ProxyRequestsAdapter<P> {
    ProxyRequestsPlayerSender getPlayerSender(P player);
    boolean sendPluginMessage(String server, byte[] message);
    boolean sendPluginMessage(byte[] message);
    Vault getVault();
}
