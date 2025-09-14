package de.janschuri.lunaticlib.sender.platform;

import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;

import java.util.UUID;

public interface SenderPlatform<P, T> {
    Sender getSender(T sender);
    PlayerSender getPlayerSender(UUID uuid);
}
