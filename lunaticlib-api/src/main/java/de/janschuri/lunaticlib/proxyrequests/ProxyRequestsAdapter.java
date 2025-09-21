package de.janschuri.lunaticlib.proxyrequests;

import de.janschuri.lunaticlib.proxyrequests.external.Vault;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsSender;
import de.janschuri.lunaticlib.sender.SenderAdapter;

import java.util.UUID;

public interface ProxyRequestsAdapter<T> extends SenderAdapter<T> {
    @Override
    ProxyRequestsSender getSender(T sender);
    @Override
    ProxyRequestsPlayerSender getPlayerSender(UUID uuid);
    boolean sendPluginMessage(String server, byte[] message);
    boolean sendPluginMessage(byte[] message);
    Vault getVault();
}
