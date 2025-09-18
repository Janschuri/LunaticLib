package de.janschuri.lunaticlib.commands;

import de.janschuri.lunaticlib.sender.PlayerSender;

import java.util.Collection;

public interface CommandAdapter<P> {
    void registerCommand(P plugin, Command command);
    Collection<PlayerSender> getOnlinePlayers();
}
