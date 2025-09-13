package de.janschuri.lunaticlib.commands.platform;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;

import java.util.Collection;

public interface Platform<P, T> {
    void registerCommand(P plugin, Command command);
    Sender getSender(T sender);
    Collection<PlayerSender> getOnlinePlayers();
}
