package de.janschuri.lunaticlib.platform;

import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.LunaticCommand;

import java.util.Collection;
import java.util.UUID;

public interface Platform<P, T> {

    boolean sendPluginMessage(String server, byte[] message);
    boolean sendPluginMessage(byte[] message);
    void sendConsoleCommand(String message);
    PlayerSender getPlayerSender(UUID uuid);
    PlatformType getPlatformType();
    Vault getVault();
    Sender getSender(T sender);
    void registerCommand(P plugin, LunaticCommand lunaticCommand);
    P getPlugin();
    Collection<PlayerSender> getOnlinePlayers();
}
