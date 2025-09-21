package de.janschuri.lunaticlib.sender;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface SenderAdapter<T> {
    Sender getSender(T sender);
    @Nullable
    PlayerSender getPlayerSender(UUID uuid);
}
