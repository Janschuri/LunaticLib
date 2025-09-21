package de.janschuri.lunaticlib.commands;

import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.SenderAdapter;

import java.util.Collection;

public interface CommandAdapter<P, T> extends SenderAdapter<T> {
    void registerCommand(P plugin, Command command);
    Collection<PlayerSender> getOnlinePlayers();
}
